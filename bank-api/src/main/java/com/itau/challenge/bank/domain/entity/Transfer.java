package com.itau.challenge.bank.domain.entity;

import java.math.BigDecimal;

public record Transfer(boolean success,
                       String message,
                       boolean bacenNotified,
                       BigDecimal remainingBalance) {
}
