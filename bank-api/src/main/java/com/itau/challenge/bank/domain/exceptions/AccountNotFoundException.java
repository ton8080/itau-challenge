package com.itau.challenge.bank.domain.exceptions;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException() {
        super();
    }

    public AccountNotFoundException(String accountId) {
        super("Account not found: " + accountId);
    }

    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}