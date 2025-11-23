package com.itau.challenge.bank.infrastructure.provider;

import com.itau.challenge.bank.domain.entity.Account;
import com.itau.challenge.bank.domain.provider.SaveAccountProvider;
import com.itau.challenge.bank.infrastructure.mapper.AccountMapper;
import com.itau.challenge.bank.infrastructure.persistence.model.AccountModel;
import com.itau.challenge.bank.infrastructure.persistence.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class SaveAccountProviderImpl implements SaveAccountProvider {
    private final AccountRepository repository;
    private final AccountMapper accountMapper;

    public SaveAccountProviderImpl(AccountRepository repository, AccountMapper accountMapper) {
        this.repository = repository;
        this.accountMapper = accountMapper;
    }

    @Override
    public Account save(Account account) {
        return java.util.stream.Stream.ofNullable(account)
                .map(accountMapper::toModel)
                .map(repository::save)
                .map(accountMapper::toDomain)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Failed to save account"));
    }

}
