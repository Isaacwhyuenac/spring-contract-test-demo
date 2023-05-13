package com.example.bankservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@Configuration
class ContractTestsConfiguration {

  @Bean
  KafkaEventVerifier kafkaEventVerifier() {
    return new KafkaEventVerifier();
  }
}