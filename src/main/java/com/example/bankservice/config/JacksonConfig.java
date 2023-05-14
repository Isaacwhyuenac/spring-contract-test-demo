package com.example.bankservice.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JacksonConfig {

  @Bean
  public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilderCustomizer() {
    return new Jackson2ObjectMapperBuilder()
      .modulesToInstall(new JavaTimeModule())
//      .serializationInclusion(JsonInclude.Include.NON_EMPTY) // This removes null/ empty list from the response
      .featuresToDisable(
        SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS,
        DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
      )
      .featuresToEnable(
        DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
        DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT
      )
      .indentOutput(false);
  }

}
