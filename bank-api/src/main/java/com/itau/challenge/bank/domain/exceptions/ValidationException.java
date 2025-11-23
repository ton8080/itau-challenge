package com.itau.challenge.bank.domain.exceptions;

import java.math.BigDecimal;

public class ValidationException extends RuntimeException {
    private final BigDecimal remainingBalance;

    public ValidationException(String message, BigDecimal remainingBalance) {
        super(message);
        this.remainingBalance = remainingBalance;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }
}
