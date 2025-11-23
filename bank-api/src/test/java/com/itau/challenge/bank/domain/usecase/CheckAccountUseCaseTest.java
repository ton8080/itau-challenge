package com.itau.challenge.bank.domain.usecase;

import com.itau.challenge.bank.domain.entity.Account;
import com.itau.challenge.bank.domain.exceptions.AccountNotFoundException;
import com.itau.challenge.bank.domain.provider.FindAccountByIdProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckAccountUseCaseTest {

    @Mock
    private FindAccountByIdProvider findAccountByIdProvider;

    private CheckAccountUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CheckAccountUseCase(findAccountByIdProvider);
    }

    @Test
    void returnsAccountWhenFound() {
        String accountId = "acc-123";
        Account account = mock(Account.class);
        when(findAccountByIdProvider.findByIdForUpdate(accountId)).thenReturn(Optional.of(account));

        Account result = useCase.checkAccount(accountId);

        assertSame(account, result);
        verify(findAccountByIdProvider, times(1)).findByIdForUpdate(accountId);
    }

    @Test
    void throwsWhenAccountNotFound() {
        String accountId = "missing";
        when(findAccountByIdProvider.findByIdForUpdate(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> useCase.checkAccount(accountId));
        verify(findAccountByIdProvider, times(1)).findByIdForUpdate(accountId);
    }
}
