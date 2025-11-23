package com.itau.challenge.bank.domain.utils.validator;

import com.itau.challenge.bank.domain.entity.Account;

import java.math.BigDecimal;

public class CustomerValidator implements TransferValidator {
    private TransferValidator next;

    @Override
    public TransferValidator setNext(TransferValidator next) {
        this.next = next;
        return next;
    }

    @Override
    public ValidationResult validate(Account acc, BigDecimal amount) {
        if (acc.getCustomerName() == null) {
            return ValidationResult.fail("Customer not found");
        }
        return next != null ? next.validate(acc, amount) : ValidationResult.ok();
    }
}
