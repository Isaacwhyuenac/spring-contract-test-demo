package com.example.bankservice.controller.services;

import org.springframework.stereotype.Service;

import com.example.bankservice.mq.sender.KafkaSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

  private final KafkaSender kafkaSender;

  public void createTransaction() {

  }

}
