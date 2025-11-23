package com.itau.challenge.bank.domain.provider;

import com.itau.challenge.bank.domain.entity.Account;

public interface GetCustomerByIdProvider {
    Account getCustomerById(String id);
}
