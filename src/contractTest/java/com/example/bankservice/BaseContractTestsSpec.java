package com.example.bankservice;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierReceiver;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.DefaultKafkaHeaderMapper;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.example.bankservice.model.Records;
import com.example.bankservice.mq.sender.KafkaSender;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest(
  webEnvironment = WebEnvironment.NONE,
  classes = {
    BaseContractTestsSpec.ContractTestsConfiguration.class,
    BankServiceApplication.class,
  }
)
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureMessageVerifier
public abstract class BaseContractTestsSpec {
  @Container
  private static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

  @Autowired
  private KafkaSender kafkaSender;

  @DynamicPropertySource
  static void kafkaProperties(DynamicPropertyRegistry registry) {
    kafka.start();
    registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
  }

  public void createBankTransaction() {
    kafkaSender.send(
      Records.builder()
        .amount("$100")
        .description("What the fuck am I doing in HK")
        .iban(UUID.randomUUID().toString())
        .build()
    );

  }

  @EnableKafka
  @Configuration
  static class ContractTestsConfiguration {

    @Bean
    KafkaEventVerifier kafkaEventMessageVerifier() {
      return new KafkaEventVerifier();
    }

  }


  @Slf4j
  static class KafkaEventVerifier implements MessageVerifierReceiver<Message<?>> {

    private final Set<Message> consumedEvents = Collections.synchronizedSet(new HashSet<>());
    Map<String, BlockingQueue<Message<?>>> broker = new ConcurrentHashMap<>();


    @KafkaListener(id = "transactionEvents",
      topics = {"transaction-events"},
//      topicPattern = ".*",
      groupId = "order-consumer"
    )
    void transactionEvents(ConsumerRecord payload, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
      consumedEvents.add(MessageBuilder.createMessage(payload.value(), new MessageHeaders(Collections.emptyMap())));

//      Map<String, Object> headers = new HashMap<>();
//      new DefaultKafkaHeaderMapper().toHeaders(payload.headers(), headers);
//      broker.putIfAbsent(topic, new ArrayBlockingQueue<>(1));
//      BlockingQueue<Message<?>> messageQueue = broker.get(topic);
//      messageQueue.add(MessageBuilder.createMessage(payload.value(), new MessageHeaders(headers)));
    }

    @SneakyThrows
    @Override
    public Message receive(String destination, long timeout, TimeUnit timeUnit, @Nullable YamlContract contract) {
      log.info("Waiting for message on topic {}", destination);
      for (int i = 0; i < timeout; i++) {
        Message msg = consumedEvents.stream().findFirst().orElse(null);
        if (msg != null) {
          return msg;
        }

        timeUnit.sleep(1);
      }

      return consumedEvents.stream().findFirst().orElse(null);

//      broker.putIfAbsent(destination, new ArrayBlockingQueue<>(1));
//      BlockingQueue<Message<?>> messageQueue = broker.get(destination);
//      Message<?> message;
//      try {
//        message = messageQueue.poll(timeout, timeUnit);
//      } catch (InterruptedException e) {
//        throw new RuntimeException(e);
//      }
//      if (message != null) {
//        log.info("Removed a message from a topic [" + destination + "]");
//      }
//      return message;
    }

    @Override
    public Message receive(String destination, YamlContract contract) {
      return receive(destination, 20, TimeUnit.SECONDS, contract);
    }

  }

}
