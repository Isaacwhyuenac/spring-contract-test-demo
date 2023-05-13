package com.example.bankservice.mq.sender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.bankservice.model.Records;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaSender {

  @Value("${kafka.topic.transactions:transaction-events}")
  private String topic;

  private KafkaTemplate<String, Object> kafkaTemplate;

  public void send(Records transaction) {

  }
}
