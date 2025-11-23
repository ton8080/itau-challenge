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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAccountByIdProviderImplTest {

    @Mock
    private AccountRepository repository;
    @Mock
    private AccountMapper accountMapper;

    private FindAccountByIdProviderImpl provider;

    @BeforeEach
    void setUp() {
        provider = new FindAccountByIdProviderImpl(repository, accountMapper);
    }

    @Test
    void findByIdForUpdate_whenFound_returnsMappedAccount() {
        UUID id = UUID.randomUUID();

        AccountModel model = new AccountModel();
        model.setId(id);

        Account mapped = new Account();
        mapped.setId(id);

        when(repository.findByIdForUpdate(eq(id))).thenReturn(Optional.of(model));
        when(accountMapper.toDomain(model)).thenReturn(mapped);

        Optional<Account> result = provider.findByIdForUpdate(id.toString());

        assertTrue(result.isPresent());
        assertSame(mapped, result.get());
        verify(repository, times(1)).findByIdForUpdate(eq(id));
        verify(accountMapper, times(1)).toDomain(model);
    }

    @Test
    void findByIdForUpdate_whenNotFound_returnsEmpty() {
        UUID id = UUID.randomUUID();

        when(repository.findByIdForUpdate(eq(id))).thenReturn(Optional.empty());

        Optional<Account> result = provider.findByIdForUpdate(id.toString());

        assertFalse(result.isPresent());
        verify(repository, times(1)).findByIdForUpdate(eq(id));
        verifyNoInteractions(accountMapper);
    }
}
