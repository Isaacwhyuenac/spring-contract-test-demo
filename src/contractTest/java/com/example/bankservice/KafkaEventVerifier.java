//package com.example.bankservice;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.TimeUnit;
//
//import javax.annotation.Nullable;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.cloud.contract.verifier.converter.YamlContract;
//import org.springframework.cloud.contract.verifier.messaging.MessageVerifierReceiver;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.DefaultKafkaHeaderMapper;
//import org.springframework.kafka.support.KafkaHeaders;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageHeaders;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.messaging.support.MessageBuilder;
//
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//class KafkaEventVerifier implements MessageVerifierReceiver<Message<?>> {
//
//  private final Set<Message> consumedEvents = Collections.synchronizedSet(new HashSet<>());
//
//
//  @KafkaListener(id = "transactionEvents", topicPattern = ".*", groupId = "order-consumer")
//  void transactionEvents(ConsumerRecord payload, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
//    consumedEvents.add(MessageBuilder.createMessage(payload.value(), new MessageHeaders(Collections.emptyMap())));
//  }
//
//  @SneakyThrows
//  @Override
//  public Message receive(String destination, long timeout, TimeUnit timeUnit, @Nullable YamlContract contract) {
//    log.info("Waiting for message on topic {}", destination);
//    for (int i = 0; i < timeout; i++) {
//      Message msg = consumedEvents.stream().findFirst().orElse(null);
//      if (msg != null) {
//        return msg;
//      }
//
//      timeUnit.sleep(1);
//    }
//
//    return consumedEvents.stream().findFirst().orElse(null);
//
//  }
//
//  @Override
//  public Message receive(String destination, YamlContract contract) {
//    return receive(destination, 15, TimeUnit.SECONDS, contract);
//  }
//
//}