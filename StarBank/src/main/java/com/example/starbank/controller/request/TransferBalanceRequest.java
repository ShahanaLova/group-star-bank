package com.example.starbank.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferBalanceRequest {
    private String fromCardNumber;

    private String toCardNumber;

    private BigDecimal amount;

}