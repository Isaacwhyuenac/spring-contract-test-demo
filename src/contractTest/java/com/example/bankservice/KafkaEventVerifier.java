package com.example.bankservice;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifierReceiver;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import lombok.SneakyThrows;

class KafkaEventVerifier implements MessageVerifierReceiver<Message<?>> {

  private final Set<Message> consumedEvents = Collections.synchronizedSet(new HashSet<>());

  @KafkaListener(topics = {"transactionEvents"}, groupId = "order-consumer")
  void transactionEvents(ConsumerRecord payload) {
    consumedEvents.add(MessageBuilder.createMessage(payload.value(), new MessageHeaders(Collections.emptyMap())));
  }

  @SneakyThrows
  @Override
  public Message receive(String destination, long timeout, TimeUnit timeUnit, @Nullable YamlContract contract) {
    for (int i = 0; i < timeout; i++) {
      Message msg = consumedEvents.stream().findFirst().orElse(null);
      if (msg != null) {
        return msg;
      }

      timeUnit.sleep(1);
    }

    return consumedEvents.stream().findFirst().orElse(null);
  }

  @Override
  public Message receive(String destination, YamlContract contract) {
    return receive(destination, 5, TimeUnit.SECONDS, contract);
  }

}