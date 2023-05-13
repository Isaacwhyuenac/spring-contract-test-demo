package com.example.bankservice.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Records implements Serializable {
  private static final long serialVersionUID = -7269057310808799116L;

  private String amount;

  private String iban;

  private String description;

}
