package com.itau.challenge.bank.infrastructure.provider;

import com.itau.challenge.bank.domain.entity.Account;
import com.itau.challenge.bank.infrastructure.mapper.AccountMapper;
import com.itau.challenge.bank.infrastructure.persistence.model.AccountModel;
import com.itau.challenge.bank.infrastructure.persistence.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveAccountProviderImplTest {

    @Mock
    private AccountRepository repository;
    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private SaveAccountProviderImpl provider;


    @Test
    void save_whenAccountNotNull_shouldReturnSavedDomainAccount() {
        UUID accountId = UUID.randomUUID();
        Account inputAccount = new Account();
        inputAccount.setId(accountId);

        AccountModel model = new AccountModel();
        model.setId(accountId);

        Account savedDomain = new Account();
        savedDomain.setId(accountId);

        when(accountMapper.toModel(eq(inputAccount))).thenReturn(model);
        when(repository.save(eq(model))).thenReturn(model);
        when(accountMapper.toDomain(eq(model))).thenReturn(savedDomain);

        Account result = provider.save(inputAccount);

        assertNotNull(result);
        assertSame(savedDomain, result);
        verify(accountMapper, times(1)).toModel(eq(inputAccount));
        verify(repository, times(1)).save(eq(model));
        verify(accountMapper, times(1)).toDomain(eq(model));
    }

    @Test
    void save_whenAccountIsNull_shouldThrowRuntimeException() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> provider.save(null));
        assertEquals("Failed to save account", ex.getMessage());

        verifyNoInteractions(accountMapper);
        verifyNoInteractions(repository);
    }
}
