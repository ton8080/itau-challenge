package com.itau.challenge.bank.infrastructure.mapper;

import com.itau.challenge.bank.domain.entity.Account;
import com.itau.challenge.bank.infrastructure.persistence.model.AccountModel;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class AccountMapper {

   public Account toDomain(AccountModel accountModel) {
        if (accountModel == null) return null;

        Account account = new Account();
        account.setId(accountModel.getId());
        account.setAccountNumber(accountModel.getAccountNumber());
        account.setActiveAccount(accountModel.isActiveAccount());
        account.setBalance(accountModel.getBalance() == null ? BigDecimal.ZERO : accountModel.getBalance());
        account.setDailyTransferred(accountModel.getDailyTransferred() == null ? BigDecimal.ZERO : accountModel.getDailyTransferred());
        account.setCustomerName(accountModel.getCustomerName());
        account.setLastDailyReset(accountModel.getLastDailyReset());

        return account;
    }

    public  AccountModel toModel(Account account) {
        if (account == null) return null;

        AccountModel model = new AccountModel();
        model.setId(account.getId());
        model.setAccountNumber(account.getAccountNumber());
        model.setActiveAccount(account.isActiveAccount());
        model.setBalance(account.getBalance() == null ? BigDecimal.ZERO : account.getBalance());
        model.setDailyTransferred(account.getDailyTransferred() == null ? BigDecimal.ZERO : account.getDailyTransferred());
        model.setCustomerName(account.getCustomerName());
        model.setLastDailyReset(account.getLastDailyReset() == null ? LocalDate.now() : account.getLastDailyReset());

        return model;
    }
}
