package com.example.bankservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankservice.controller.services.TransactionService;
import com.example.bankservice.model.Records;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TransactionController {

  private final TransactionService transactionService;

  @GetMapping("/transaction")
  public ResponseEntity<String> transaction(@RequestBody Records transaction) {
    transactionService.createTransaction(transaction);

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

}
