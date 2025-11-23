package com.itau.challenge.bank.domain.usecase;

import com.itau.challenge.bank.domain.entity.Account;
import com.itau.challenge.bank.domain.exceptions.AccountNotFoundException;
import com.itau.challenge.bank.domain.provider.FindAccountByIdProvider;

public class CheckAccountUseCase {
    private final FindAccountByIdProvider findAccountByIdProvider;

    public CheckAccountUseCase(FindAccountByIdProvider findAccountByIdProvider) {
        this.findAccountByIdProvider = findAccountByIdProvider;
    }

    public Account checkAccount(String accountId) {
        return findAccountByIdProvider.findByIdForUpdate(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }
}
