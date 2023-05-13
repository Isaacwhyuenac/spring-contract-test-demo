package com.example.bankservice;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.example.bankservice.BankServiceApplication;
import com.example.bankservice.model.Records;
import com.example.bankservice.mq.sender.KafkaSender;

@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = {BankServiceApplication.class})
@ActiveProfiles("test")
@AutoConfigureMessageVerifier
public class BaseContractTestSpec {
  @Container
  private static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));

  @Autowired
  private KafkaSender kafkaSender;

  @DynamicPropertySource
  static void kafkaProperties(DynamicPropertyRegistry registry) {
    kafka.start();
    registry.add("spring.kafka.bootstrap-servers", () -> kafka.getBootstrapServers());
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

}
