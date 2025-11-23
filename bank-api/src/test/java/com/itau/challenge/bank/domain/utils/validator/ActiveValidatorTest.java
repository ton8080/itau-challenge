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
class ActiveValidatorTest {

    @Mock
    private TransferValidator next;

    @Test
    void validate_activeAccount_withoutNext_returnsOk() {
        ActiveValidator validator = new ActiveValidator();
        Account acc = new Account(UUID.randomUUID(), "acc-1", true, new BigDecimal("100.00"), BigDecimal.ZERO, "Cliente");

        ValidationResult result = validator.validate(acc, new BigDecimal("10.00"));

        assertTrue(result.isValid());
    }

    @Test
    void validate_inactiveAccount_returnsFailWithMessage() {
        ActiveValidator validator = new ActiveValidator();
        Account acc = new Account(UUID.randomUUID(), "acc-2", false, new BigDecimal("50.00"), BigDecimal.ZERO, "Cliente");

        ValidationResult result = validator.validate(acc, new BigDecimal("5.00"));

        assertFalse(result.isValid());
        assertEquals("Account is not active", result.getMessage());
    }

    @Test
    void validate_activeAccount_delegatesToNext_andReturnsNextResult_onFailure() {
        ActiveValidator validator = new ActiveValidator();
        validator.setNext(next);

        Account acc = new Account(UUID.randomUUID(), "acc-3", true, new BigDecimal("100.00"), BigDecimal.ZERO, "Cliente");
        ValidationResult nextResult = ValidationResult.fail("next failed");
        when(next.validate(acc, new BigDecimal("1.00"))).thenReturn(nextResult);

        ValidationResult result = validator.validate(acc, new BigDecimal("1.00"));

        assertSame(nextResult, result);
        verify(next, times(1)).validate(acc, new BigDecimal("1.00"));
    }

    @Test
    void validate_activeAccount_delegatesToNext_andReturnsNextResult_onSuccess() {
        ActiveValidator validator = new ActiveValidator();
        validator.setNext(next);

        Account acc = new Account(UUID.randomUUID(), "acc-4", true, new BigDecimal("200.00"), BigDecimal.ZERO, "Cliente");
        ValidationResult nextResult = ValidationResult.ok();
        when(next.validate(acc, new BigDecimal("2.00"))).thenReturn(nextResult);

        ValidationResult result = validator.validate(acc, new BigDecimal("2.00"));

        assertSame(nextResult, result);
        verify(next, times(1)).validate(acc, new BigDecimal("2.00"));
    }
}
