package com.itau.challenge.bank.infrastructure.provider;

import com.itau.challenge.bank.domain.entity.Account;
import com.itau.challenge.bank.domain.provider.FindAccountByIdProvider;
import com.itau.challenge.bank.infrastructure.mapper.AccountMapper;
import com.itau.challenge.bank.infrastructure.persistence.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class FindAccountByIdProviderImpl implements FindAccountByIdProvider {
    private final AccountRepository repository;
    private final AccountMapper accountMapper;

    public FindAccountByIdProviderImpl(AccountRepository repository, AccountMapper accountMapper) {
        this.repository = repository;
        this.accountMapper = accountMapper;
    }

    @Override
    public Optional<Account> findByIdForUpdate(String accountId) {
        return repository.findByIdForUpdate(UUID.fromString(accountId))
                .map(accountMapper::toDomain);
    }
}
