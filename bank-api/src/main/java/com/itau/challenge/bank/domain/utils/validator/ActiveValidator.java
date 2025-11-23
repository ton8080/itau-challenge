package com.itau.challenge.bank.domain.utils.validator;

import java.math.BigDecimal;
import com.itau.challenge.bank.domain.entity.Account;

public class ActiveValidator implements TransferValidator {
    private TransferValidator next;

    @Override
    public TransferValidator setNext(TransferValidator next) {
        this.next = next;
        return next;
    }

    @Override
    public ValidationResult validate(Account acc, BigDecimal amount) {
        if (!acc.isActiveAccount()) return ValidationResult.fail("Account is not active");
        return next != null ? next.validate(acc, amount) : ValidationResult.ok();
    }
}
