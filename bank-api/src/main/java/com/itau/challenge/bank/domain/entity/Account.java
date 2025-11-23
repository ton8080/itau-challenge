package com.itau.challenge.bank.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Account {

    private UUID id;
    private String accountNumber;
    private boolean activeAccount;
    private BigDecimal balance;
    private BigDecimal dailyTransferred;
    private String customerName;
    private LocalDate lastDailyReset;

    public Account(UUID id,
                   String accountNumber,
                   boolean active,
                   BigDecimal balance,
                   BigDecimal dailyTransferred,
                   String customerName) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.activeAccount = active;
        this.balance = balance == null ? BigDecimal.ZERO : balance;
        this.dailyTransferred = dailyTransferred == null ? BigDecimal.ZERO : dailyTransferred;
        this.customerName = customerName;
    }

    public Account() {
    }

    public Account debit(BigDecimal amount) {
        BigDecimal newBalance = this.balance.subtract(amount);
        return new Account(id, accountNumber, activeAccount, newBalance, dailyTransferred.add(amount), customerName);
    }

    public Account credit(BigDecimal amount) {
        BigDecimal newBalance = this.balance.add(amount);
        return new Account(id, accountNumber, activeAccount, newBalance, dailyTransferred, customerName);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public boolean isActiveAccount() {
        return activeAccount;
    }

    public void setActiveAccount(boolean activeAccount) {
        this.activeAccount = activeAccount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getDailyTransferred() {
        return dailyTransferred;
    }

    public void setDailyTransferred(BigDecimal dailyTransferred) {
        this.dailyTransferred = dailyTransferred;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getLastDailyReset() {
        return lastDailyReset;
    }

    public void setLastDailyReset(LocalDate lastDailyReset) {
        this.lastDailyReset = lastDailyReset;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", activeAccount=" + activeAccount +
                ", balance=" + balance +
                ", dailyTransferred=" + dailyTransferred +
                ", customerName='" + customerName + '\'' +
                ", lastDailyReset=" + lastDailyReset +
                '}';
    }

    public void applyDailyReset() {
    }
}
