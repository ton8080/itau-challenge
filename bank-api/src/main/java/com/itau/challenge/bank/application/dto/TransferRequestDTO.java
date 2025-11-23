package com.itau.challenge.bank.application.dto;

import java.math.BigDecimal;

public record TransferRequestDTO(String fromAccountId, BigDecimal amount, String toAccountNumber){
}
