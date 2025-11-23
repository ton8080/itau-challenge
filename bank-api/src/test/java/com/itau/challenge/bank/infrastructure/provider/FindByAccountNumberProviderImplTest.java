package com.itau.challenge.bank.infrastructure.provider;

import com.itau.challenge.bank.domain.entity.Account;
import com.itau.challenge.bank.infrastructure.mapper.AccountMapper;
import com.itau.challenge.bank.infrastructure.persistence.model.AccountModel;
import com.itau.challenge.bank.infrastructure.persistence.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindByAccountNumberProviderImplTest {

    @Mock
    private AccountRepository repository;
    @Mock
    private AccountMapper mapper;

    private FindByAccountNumberProviderImpl provider;

    @BeforeEach
    void setUp() {
        provider = new FindByAccountNumberProviderImpl(repository, mapper);
    }

    @Test
    void findByAccountNumberForUpdate_whenFound_returnsMappedAccount() {
        String accountNumber = "ACC-123";
        AccountModel model = new AccountModel();
        model.setAccountNumber(accountNumber);

        Account mapped = new Account();
        mapped.setAccountNumber(accountNumber);

        when(repository.findByAccountNumberForUpdate(eq(accountNumber))).thenReturn(Optional.of(model));
        when(mapper.toDomain(model)).thenReturn(mapped);

        Optional<Account> result = provider.findByAccountNumberForUpdate(accountNumber);

        assertTrue(result.isPresent());
        assertSame(mapped, result.get());
        verify(repository, times(1)).findByAccountNumberForUpdate(eq(accountNumber));
        verify(mapper, times(1)).toDomain(model);
    }

    @Test
    void findByAccountNumberForUpdate_whenNotFound_returnsEmpty() {
        String accountNumber = "ACC-999";

        when(repository.findByAccountNumberForUpdate(eq(accountNumber))).thenReturn(Optional.empty());

        Optional<Account> result = provider.findByAccountNumberForUpdate(accountNumber);

        assertFalse(result.isPresent());
        verify(repository, times(1)).findByAccountNumberForUpdate(eq(accountNumber));
        verifyNoInteractions(mapper);
    }
}
