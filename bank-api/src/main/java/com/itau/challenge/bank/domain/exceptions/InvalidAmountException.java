package com.itau.challenge.bank.domain.exceptions;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException() {
        super();
    }

    public InvalidAmountException(String message) {
        super(message);
    }

    public InvalidAmountException(String message, Throwable cause) {
        super(message, cause);
    }
}
