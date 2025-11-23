package com.itau.challenge.bank.infrastructure.provider;

import com.itau.challenge.bank.domain.entity.Account;
import com.itau.challenge.bank.domain.provider.FindByAccountNumberProvider;
import com.itau.challenge.bank.infrastructure.mapper.AccountMapper;
import com.itau.challenge.bank.infrastructure.persistence.repository.AccountRepository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FindByAccountNumberProviderImpl implements FindByAccountNumberProvider {

    private final AccountRepository repository;
    private final AccountMapper mapper;

    public FindByAccountNumberProviderImpl(AccountRepository repository,
                                           AccountMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Account> findByAccountNumberForUpdate(String targetAccountNumber) {
        return repository.findByAccountNumberForUpdate(targetAccountNumber).map
                (mapper::toDomain);
    }
}
