package com.example.bankservice.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter;
import org.springframework.kafka.support.converter.BytesJsonMessageConverter;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.ProjectingMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.JsonSerializer;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class KafkaConfig {

  @Value("${kafka.topic.transactions:transaction-events}")
  private String topic;

  @Bean
  public RecordMessageConverter converter() {
    StringJsonMessageConverter converter = new StringJsonMessageConverter();
//    DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
//    typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
//    typeMapper.addTrustedPackages("*");
//    Map<String, Class<?>> mappings = new HashMap<>();
////    mappings.put("greeting", Greeting.class);
////    mappings.put("farewell", Farewell.class);
//    typeMapper.setIdClassMapping(mappings);
//    converter.setTypeMapper(typeMapper);

    return converter;
//    return new JsonMessageConverter();
//    return new StringJsonMessageConverter();
//    return new ByteArrayJsonMessageConverter();
//    return new BytesJsonMessageConverter();
//    return new ProjectingMessageConverter();
  }

  @Bean
  public ProducerFactory<Object, Object> kafkaProducerFactory(KafkaProperties properties, Jackson2ObjectMapperBuilder objectMapperBuilder) {
    JsonSerializer<Object> jsonSerializer = new JsonSerializer<>(objectMapperBuilder.build());
    return new DefaultKafkaProducerFactory<>(properties.buildProducerProperties(), jsonSerializer, jsonSerializer);
  }

  @Bean
  public KafkaTemplate kafkaTemplate(ProducerFactory<Object, Object> kafkaProducerFactory, RecordMessageConverter converter) {
    KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
    kafkaTemplate.setMessageConverter(converter);
    return kafkaTemplate;
  }

//  @Bean
//  public NewTopic transactionEvents() {
//    return new NewTopic(topic, 1, (short) 1);
//  }


}
