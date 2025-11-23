package com.itau.challenge.bank.infrastructure.persistence.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class AccountModel {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "active_account")
    private boolean activeAccount;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "daily_transferred", nullable = false)
    private BigDecimal dailyTransferred = BigDecimal.ZERO;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "daily_reset", nullable = false)
    private LocalDate lastDailyReset;

    public AccountModel() {
    }

    public AccountModel(UUID id, String accountNumber, boolean activeAccount, BigDecimal balance, BigDecimal dailyTransferred, String customerName, LocalDate lastDailyReset) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.activeAccount = activeAccount;
        this.balance = balance;
        this.dailyTransferred = dailyTransferred;
        this.customerName = customerName;
        this.lastDailyReset = lastDailyReset;
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
}
