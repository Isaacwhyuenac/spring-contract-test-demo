package com.example.bankservice.controller.services;

import java.util.Map;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.example.bankservice.model.Records;
import com.example.bankservice.mq.sender.KafkaSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

  private final KafkaSender kafkaSender;

  public void createTransaction(Records transaction) {

    kafkaSender.send(transaction);
  }

}
