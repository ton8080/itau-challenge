package com.itau.challenge.bank.application.service;

import com.itau.challenge.bank.application.dto.CheckAccountResponseDTO;
import com.itau.challenge.bank.application.mapper.AccountAppMapper;
import com.itau.challenge.bank.domain.usecase.CheckAccountUseCase;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Stream;

@Service
public class AccountService {

    private final CheckAccountUseCase checkAccountUseCase;
    private final AccountAppMapper accountAppMapper;

    public AccountService(CheckAccountUseCase checkAccountUseCase, AccountAppMapper accountAppMapper) {
        this.checkAccountUseCase = checkAccountUseCase;
        this.accountAppMapper = accountAppMapper;
    }

    public CheckAccountResponseDTO checkAccount(UUID accountId) {
        return Stream.of(checkAccountUseCase.checkAccount(accountId.toString()))
                .map(accountAppMapper::toDto)
                .findFirst()
                .orElse(null);
    }
}
