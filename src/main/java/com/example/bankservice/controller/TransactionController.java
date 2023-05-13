package com.example.bankservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankservice.controller.services.TransactionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TransactionController {

  private final TransactionService transactionService;

  @GetMapping("/transaction")
  public ResponseEntity<String> transaction(@RequestBody String ja) {
    transactionService.createTransaction();

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

}
