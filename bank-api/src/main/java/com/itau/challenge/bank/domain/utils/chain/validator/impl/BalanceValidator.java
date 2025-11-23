package com.itau.challenge.bank.domain.utils.chain.validator.impl;

import java.math.BigDecimal;
import com.itau.challenge.bank.domain.entity.Account;
import com.itau.challenge.bank.domain.utils.chain.validator.TransferValidator;
import com.itau.challenge.bank.domain.utils.chain.validator.ValidationResult;

public class BalanceValidator implements TransferValidator {
    private TransferValidator next;

    @Override
    public TransferValidator setNext(TransferValidator next) {
        this.next = next;
        return next;
    }

    @Override
    public ValidationResult validate(Account acc, BigDecimal amount) {
        if (acc.getBalance().compareTo(amount) < 0) {
            return ValidationResult.fail("Insufficient balance");
        }
        return next != null ? next.validate(acc, amount) : ValidationResult.ok();
    }
}
