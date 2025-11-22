package com.itau.challenge.bank.domain.entity;

import java.math.BigDecimal;

public record Account(String id,
                      String accountNumber,
                      boolean active,
                      BigDecimal balance,
                      BigDecimal dailyTransferred,
                      String customerName) {

    public Account {
        if (balance == null) balance = BigDecimal.ZERO;
        if (dailyTransferred == null) dailyTransferred = BigDecimal.ZERO;
    }

    public Account debit(BigDecimal amount) {
        if (amount == null) throw new IllegalArgumentException("amount is required");
        BigDecimal newBalance = this.balance.subtract(amount);
        BigDecimal newDaily = this.dailyTransferred.add(amount);
        return new Account(id, accountNumber, active, newBalance, newDaily, this.customerName);
    }

    public Account credit(BigDecimal amount) {
        if (amount == null) throw new IllegalArgumentException("amount is required");
        BigDecimal newBalance = this.balance.add(amount);
        return new Account(id, accountNumber, active, newBalance, dailyTransferred, this.customerName);
    }

}
