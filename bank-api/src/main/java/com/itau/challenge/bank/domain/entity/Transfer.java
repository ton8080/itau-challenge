package com.itau.challenge.bank.domain.entity;

import java.math.BigDecimal;

public class Transfer {

    private  boolean success;
    private  String message;
    private  boolean bacenNotified;
    private  BigDecimal remainingBalance;

    public Transfer(boolean success, String message, boolean bacenNotified, BigDecimal remainingBalance) {
        this.success = success;
        this.message = message;
        this.bacenNotified = bacenNotified;
        this.remainingBalance = remainingBalance == null ? BigDecimal.ZERO : remainingBalance;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isBacenNotified() {
        return bacenNotified;
    }

    public void setBacenNotified(boolean bacenNotified) {
        this.bacenNotified = bacenNotified;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }
}
