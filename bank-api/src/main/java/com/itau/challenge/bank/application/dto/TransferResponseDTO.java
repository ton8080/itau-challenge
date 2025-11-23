package com.itau.challenge.bank.application.dto;

import java.math.BigDecimal;

public record TransferResponseDTO(
        boolean success,
        String message,
        boolean bacenNotified,
        BigDecimal remainingBalance
) {
}
