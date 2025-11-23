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
class AmountValidatorTest {

    @Mock
    private TransferValidator next;

    @Test
    void returnsFailWhenAmountIsNull() {
        AmountValidator validator = new AmountValidator();

        ValidationResult result = validator.validate(null, null);

        assertFalse(result.isValid());
        assertEquals("Invalid amount", result.getMessage());
    }

    @Test
    void returnsFailWhenAmountIsZero() {
        AmountValidator validator = new AmountValidator();

        ValidationResult result = validator.validate(null, BigDecimal.ZERO);

        assertFalse(result.isValid());
        assertEquals("Invalid amount", result.getMessage());
    }

    @Test
    void returnsFailWhenAmountIsNegative() {
        AmountValidator validator = new AmountValidator();

        ValidationResult result = validator.validate(null, new BigDecimal("-1.00"));

        assertFalse(result.isValid());
        assertEquals("Invalid amount", result.getMessage());
    }

    @Test
    void returnsOkWhenAmountPositiveAndNoNext() {
        AmountValidator validator = new AmountValidator();

        ValidationResult result = validator.validate(null, new BigDecimal("1.00"));

        assertTrue(result.isValid());
        assertNull(result.getMessage());
    }

    @Test
    void delegatesToNextWhenAmountValid() {
        AmountValidator validator = new AmountValidator();
        validator.setNext(next);

        Account acc = new Account(UUID.randomUUID(), "acc-test", true, BigDecimal.ZERO, BigDecimal.ZERO, "C");
        BigDecimal amount = new BigDecimal("10.00");

        when(next.validate(acc, amount)).thenReturn(ValidationResult.fail("next failed"));

        ValidationResult result = validator.validate(acc, amount);

        assertFalse(result.isValid());
        assertEquals("next failed", result.getMessage());
        verify(next, times(1)).validate(acc, amount);
    }

    @Test
    void delegatesToNextAndReturnsOkWhenNextOk() {
        AmountValidator validator = new AmountValidator();
        validator.setNext(next);

        Account acc = new Account(UUID.randomUUID(), "acc-test", true, BigDecimal.ZERO, BigDecimal.ZERO, "C");
        BigDecimal amount = new BigDecimal("5.00");

        when(next.validate(acc, amount)).thenReturn(ValidationResult.ok());

        ValidationResult result = validator.validate(acc, amount);

        assertTrue(result.isValid());
        verify(next, times(1)).validate(acc, amount);
    }
}
