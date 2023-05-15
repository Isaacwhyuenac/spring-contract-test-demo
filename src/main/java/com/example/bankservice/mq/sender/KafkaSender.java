package com.example.bankservice.mq.sender;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.bankservice.model.Records;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class KafkaSender {

  @Value("${kafka.topic.transactions:transaction-events}")
  private String topic;


  private final KafkaTemplate<String, Object> kafkaTemplate;

  @SneakyThrows
  public void send(Records transaction) {
    // The json serializer will use the ObjectMapper from the Jackson2ObjectMapperBuilder
    // The json format returned includes backslashes, and jayway.jsonpath.JsonPath.parse() does not like that

    // The MessageBuilder interface is not working
//    Message<Records> message = MessageBuilder.createMessage(
//      transaction,
//      new MessageHeaders(Map.of(
//        KafkaHeaders.TOPIC, topic,
//        MessageHeaders.CONTENT_TYPE, "application/json"
//      ))
//    );

//    kafkaTemplate.send(message);

    ProducerRecord record = new ProducerRecord<>(topic, transaction);

    record.headers().add(MessageHeaders.CONTENT_TYPE, "application/json".getBytes(StandardCharsets.UTF_8));

    kafkaTemplate.send(record);
  }

}
