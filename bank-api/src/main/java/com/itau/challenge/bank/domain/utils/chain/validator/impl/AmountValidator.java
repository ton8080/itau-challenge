package com.itau.challenge.bank.domain.utils.chain.validator.impl;

import com.itau.challenge.bank.domain.entity.Account;
import com.itau.challenge.bank.domain.utils.chain.validator.TransferValidator;
import com.itau.challenge.bank.domain.utils.chain.validator.ValidationResult;

import java.math.BigDecimal;

public class AmountValidator implements TransferValidator {
    private TransferValidator next;

    @Override
    public TransferValidator setNext(TransferValidator next) {
        this.next = next;
        return next;
    }

    @Override
    public ValidationResult validate(Account acc, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return ValidationResult.fail("Invalid amount");
        }
        return next != null ? next.validate(acc, amount) : ValidationResult.ok();
    }
}
