package com.itau.challenge.bank.domain.usecase;

import com.itau.challenge.bank.domain.entity.Account;
import com.itau.challenge.bank.domain.entity.Transfer;
import com.itau.challenge.bank.domain.provider.GetCustomerByIdProvider;

import java.math.BigDecimal;

public class TransferUseCase {


    private final GetCustomerByIdProvider getCustomerByIdProvider;
    private BigDecimal DAILY_LIMIT;

    public TransferUseCase(GetCustomerByIdProvider getCustomerByIdProvider) {
        this.getCustomerByIdProvider = getCustomerByIdProvider;
    }

    public Transfer execute(String accountId, BigDecimal amount, String targetIAccount) {

     Account account = getCustomerByIdProvider.getCustomerById(accountId);

        return null;
    }
}
