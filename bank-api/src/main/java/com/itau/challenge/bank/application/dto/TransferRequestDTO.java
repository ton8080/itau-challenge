package com.itau.challenge.bank.application.dto;

import java.math.BigDecimal;

public record TransferRequestDTO(String fromAccountId, String toAccountNumber,BigDecimal amount){
}
