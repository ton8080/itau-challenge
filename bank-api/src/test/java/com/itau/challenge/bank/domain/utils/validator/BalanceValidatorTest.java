package com.itau.challenge.bank.domain.utils.validator;

import com.itau.challenge.bank.domain.entity.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BalanceValidatorTest {

    @Mock
    private TransferValidator next;

    @Test
    void validate_insufficientBalance_returnsFail() {
        BalanceValidator validator = new BalanceValidator();
        Account acc = new Account(UUID.randomUUID(), "acc-1", true, new BigDecimal("5.00"), BigDecimal.ZERO, "Cliente");

        ValidationResult result = validator.validate(acc, new BigDecimal("10.00"));

        assertFalse(result.isValid());
        assertEquals("Insufficient balance", result.getMessage());
    }

    @Test
    void validate_sufficientBalance_withoutNext_returnsOk() {
        BalanceValidator validator = new BalanceValidator();
        Account acc = new Account(UUID.randomUUID(), "acc-2", true, new BigDecimal("20.00"), BigDecimal.ZERO, "Cliente");

        ValidationResult result = validator.validate(acc, new BigDecimal("10.00"));

        assertTrue(result.isValid());
    }

    @Test
    void validate_sufficientBalance_delegatesToNext_andReturnsNextResult() {
        BalanceValidator validator = new BalanceValidator();
        validator.setNext(next);

        Account acc = new Account(UUID.randomUUID(), "acc-3", true, new BigDecimal("20.00"), BigDecimal.ZERO, "Cliente");
        ValidationResult nextResult = ValidationResult.fail("next failed");
        when(next.validate(acc, new BigDecimal("10.00"))).thenReturn(nextResult);

        ValidationResult result = validator.validate(acc, new BigDecimal("10.00"));

        assertSame(nextResult, result);
        verify(next, times(1)).validate(acc, new BigDecimal("10.00"));
    }

    @Test
    void setNext_returnsProvidedNext() {
        BalanceValidator validator = new BalanceValidator();

        TransferValidator returned = validator.setNext(next);

        assertSame(next, returned);
    }
}
