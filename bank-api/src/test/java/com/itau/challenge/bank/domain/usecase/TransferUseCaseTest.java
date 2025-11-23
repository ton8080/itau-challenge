package com.itau.challenge.bank.domain.usecase;

import com.itau.challenge.bank.domain.entity.Account;
import com.itau.challenge.bank.domain.entity.QueueMessage;
import com.itau.challenge.bank.domain.entity.Transfer;
import com.itau.challenge.bank.domain.exceptions.AccountNotFoundException;
import com.itau.challenge.bank.domain.exceptions.InvalidAmountException;
import com.itau.challenge.bank.domain.exceptions.ValidationException;
import com.itau.challenge.bank.domain.provider.BacenNotifierProvider;
import com.itau.challenge.bank.domain.provider.DailyLimitProvider;
import com.itau.challenge.bank.domain.provider.FindAccountByIdProvider;
import com.itau.challenge.bank.domain.provider.FindByAccountNumberProvider;
import com.itau.challenge.bank.domain.provider.GetCustomerByIdProvider;
import com.itau.challenge.bank.domain.provider.SaveAccountProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferUseCaseTest {

    @Mock
    private GetCustomerByIdProvider getCustomerByIdProvider;

    @Mock
    private SaveAccountProvider saveAccountProvider;

    @Mock
    private FindAccountByIdProvider findAccountByIdProvider;

    @Mock
    private FindByAccountNumberProvider findByAccountNumberProvider;

    @Mock
    private BacenNotifierProvider bacenNotifier;

    @Mock
    private DailyLimitProvider dailyLimitProvider;

    @Captor
    private ArgumentCaptor<QueueMessage> queueMessageCaptor;

    private TransferUseCase useCase;

    @BeforeEach
    void setup() {
        useCase = new TransferUseCase(
                getCustomerByIdProvider,
                saveAccountProvider,
                findAccountByIdProvider,
                findByAccountNumberProvider,
                bacenNotifier,
                dailyLimitProvider
        );
    }

    @Test
    void execute_success_notified() {
        String accountId = "acc-1";
        String targetAccountNumber = "dest-1";
        BigDecimal amount = new BigDecimal("10.00");

        Account from = new Account(UUID.randomUUID(), "src-1", true, new BigDecimal("100.00"), BigDecimal.ZERO, "C");
        Account to = new Account(UUID.randomUUID(), targetAccountNumber, true, new BigDecimal("0.00"), BigDecimal.ZERO, "D");

        Account customer = new Account(UUID.randomUUID(), "cust-1", true, BigDecimal.ZERO, BigDecimal.ZERO, "C");
        customer.setCustomerName("John Doe");
        customer.setActiveAccount(true);

        when(findAccountByIdProvider.findByIdForUpdate(accountId)).thenReturn(Optional.of(from));
        when(getCustomerByIdProvider.getCustomerById(accountId)).thenReturn(customer);
        when(dailyLimitProvider.getDailyLimit()).thenReturn(new BigDecimal("1000"));
        when(findByAccountNumberProvider.findByAccountNumberForUpdate(targetAccountNumber)).thenReturn(Optional.of(to));
        when(bacenNotifier.notifyBacen(any(QueueMessage.class))).thenReturn(true);

        Transfer result = useCase.execute(accountId, amount, targetAccountNumber);

        assertNotNull(result);
        verify(saveAccountProvider, times(2)).save(any(Account.class));
        verify(bacenNotifier, times(1)).notifyBacen(any(QueueMessage.class));
    }

    @Test
    void execute_success_bacenEnqueued_whenNotifierReturnsFalse() {
        String accountId = "acc-2";
        String targetAccountNumber = "dest-2";
        BigDecimal amount = new BigDecimal("20.00");

        Account from = new Account(UUID.randomUUID(), "src-2", true, new BigDecimal("200.00"), BigDecimal.ZERO, "C");
        Account to = new Account(UUID.randomUUID(), targetAccountNumber, true, new BigDecimal("0.00"), BigDecimal.ZERO, "D");

        Account customer = new Account(UUID.randomUUID(), "cust-2", true, BigDecimal.ZERO, BigDecimal.ZERO, "C");
        customer.setCustomerName("Jane Doe");
        customer.setActiveAccount(true);

        when(findAccountByIdProvider.findByIdForUpdate(accountId)).thenReturn(Optional.of(from));
        when(getCustomerByIdProvider.getCustomerById(accountId)).thenReturn(customer);
        when(dailyLimitProvider.getDailyLimit()).thenReturn(new BigDecimal("1000"));
        when(findByAccountNumberProvider.findByAccountNumberForUpdate(targetAccountNumber)).thenReturn(Optional.of(to));
        when(bacenNotifier.notifyBacen(any(QueueMessage.class))).thenReturn(false);

        Transfer result = useCase.execute(accountId, amount, targetAccountNumber);

        assertNotNull(result);
        verify(saveAccountProvider, times(2)).save(any(Account.class));
        verify(bacenNotifier, times(1)).notifyBacen(any(QueueMessage.class));
    }

    @Test
    void execute_validationFails_throwsValidationException_noUnnecessaryStubbing() {
        String accountId = "acc-3";
        BigDecimal amount = new BigDecimal("10.00");

        Account from = new Account(UUID.randomUUID(), "src-3", true, new BigDecimal("5.00"), BigDecimal.ZERO, "C");

        Account customer = new Account(UUID.randomUUID(), "cust-3", true, BigDecimal.ZERO, BigDecimal.ZERO, "C");
        customer.setCustomerName("Client");
        customer.setActiveAccount(true);

        when(findAccountByIdProvider.findByIdForUpdate(accountId)).thenReturn(Optional.of(from));
        when(getCustomerByIdProvider.getCustomerById(accountId)).thenReturn(customer);
        when(dailyLimitProvider.getDailyLimit()).thenReturn(new BigDecimal("1000"));

        assertThrows(ValidationException.class, () -> useCase.execute(accountId, amount, "any-dest"));
        verify(saveAccountProvider, never()).save(any());
        verify(findByAccountNumberProvider, never()).findByAccountNumberForUpdate(any());
    }

    @Test
    void execute_accountNotFound_throwsAccountNotFoundException() {
        String accountId = "missing-acc";

        when(findAccountByIdProvider.findByIdForUpdate(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> useCase.execute(accountId, new BigDecimal("1.00"), "dest"));
        verifyNoInteractions(getCustomerByIdProvider, findByAccountNumberProvider, saveAccountProvider, bacenNotifier);
    }

    @Test
    void execute_invalidAmount_throwsInvalidAmountException() {
        String accountId = "acc-4";
        BigDecimal amount = BigDecimal.ZERO; // invalid amount per AmountValidator

        Account from = new Account(UUID.randomUUID(), "src-4", true, new BigDecimal("50.00"), BigDecimal.ZERO, "C");
        Account customer = new Account(UUID.randomUUID(), "cust-4", true, BigDecimal.ZERO, BigDecimal.ZERO, "C");
        customer.setCustomerName("Client");
        customer.setActiveAccount(true);

        when(findAccountByIdProvider.findByIdForUpdate(accountId)).thenReturn(Optional.of(from));
        when(getCustomerByIdProvider.getCustomerById(accountId)).thenReturn(customer);
        when(dailyLimitProvider.getDailyLimit()).thenReturn(new BigDecimal("1000"));

        assertThrows(InvalidAmountException.class, () -> useCase.execute(accountId, amount, "dest-ignored"));
        verify(saveAccountProvider, never()).save(any());
        verify(findByAccountNumberProvider, never()).findByAccountNumberForUpdate(any());
    }
}
