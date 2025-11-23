package com.itau.challenge.bank.domain.provider;

import com.itau.challenge.bank.domain.entity.Account;

public interface SaveAccountProvider {
    Account save(Account account);
}
