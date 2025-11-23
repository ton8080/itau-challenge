package com.itau.challenge.bank.domain.provider;

import com.itau.challenge.bank.domain.entity.Account;

import java.util.Optional;

public interface FindAccountByIdProvider {
    Optional<Account> findByIdForUpdate(String accountId);
}
