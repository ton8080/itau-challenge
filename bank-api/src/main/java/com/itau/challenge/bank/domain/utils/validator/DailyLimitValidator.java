package com.itau.challenge.bank.domain.utils.validator;

import java.math.BigDecimal;
import com.itau.challenge.bank.domain.entity.Account;

public class DailyLimitValidator implements TransferValidator {

    private final BigDecimal dailyLimit;
    private TransferValidator next;

    public DailyLimitValidator(BigDecimal dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    @Override
    public TransferValidator setNext(TransferValidator next) {
        this.next = next;
        return next;
    }

    @Override
    public ValidationResult validate(Account acc, BigDecimal amount) {
        if (acc == null) {
            return ValidationResult.fail("Invalid account");
        }

        if (amount == null || amount.signum() <= 0) {
            return ValidationResult.fail("Invalid transfer amount");
        }

        BigDecimal dailyTransferred = acc.getDailyTransferred() == null ? BigDecimal.ZERO : acc.getDailyTransferred();
        BigDecimal projected = dailyTransferred.add(amount);

        if (projected.compareTo(dailyLimit) > 0) {
            BigDecimal exceededBy = projected.subtract(dailyLimit);
            BigDecimal remaining = dailyLimit.subtract(dailyTransferred);
            if (remaining.compareTo(BigDecimal.ZERO) < 0) {
                remaining = BigDecimal.ZERO;
            }
            return ValidationResult.fail("Daily transfer limit exceeded. Exceeded by " + exceededBy.toPlainString()
                    + ". Remaining " + remaining.toPlainString() + ".");
        }

        acc.setDailyTransferred(acc.getDailyTransferred().add(amount));

        return next != null ? next.validate(acc, amount) : ValidationResult.ok();
    }

}
