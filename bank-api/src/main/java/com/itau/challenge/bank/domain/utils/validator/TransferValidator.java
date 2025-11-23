package com.itau.challenge.bank.domain.utils.validator;

import com.itau.challenge.bank.domain.entity.Account;

import java.math.BigDecimal;

public interface TransferValidator {
    TransferValidator setNext(TransferValidator next);
    ValidationResult validate(Account acc, BigDecimal amount);
}
