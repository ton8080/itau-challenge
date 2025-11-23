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
class DailyLimitValidatorTest {

    @Mock
    private TransferValidator next;

    @Test
    void validate_nullAccount_returnsInvalidAccountFail() {
        DailyLimitValidator validator = new DailyLimitValidator(new BigDecimal("100.00"));

        ValidationResult result = validator.validate(null, new BigDecimal("10.00"));

        assertFalse(result.isValid());
        assertEquals("Invalid account", result.getMessage());
    }

    @Test
    void validate_nullAmount_returnsInvalidAmountFail() {
        DailyLimitValidator validator = new DailyLimitValidator(new BigDecimal("100.00"));
        Account acc = new Account(UUID.randomUUID(), "acc-1", true, new BigDecimal("50.00"), new BigDecimal("0.00"), "Cliente");

        ValidationResult result = validator.validate(acc, null);

        assertFalse(result.isValid());
        assertEquals("Invalid transfer amount", result.getMessage());
    }

    @Test
    void validate_nonPositiveAmount_returnsInvalidAmountFail() {
        DailyLimitValidator validator = new DailyLimitValidator(new BigDecimal("100.00"));
        Account acc = new Account(UUID.randomUUID(), "acc-2", true, new BigDecimal("50.00"), new BigDecimal("0.00"), "Cliente");

        ValidationResult zero = validator.validate(acc, BigDecimal.ZERO);
        ValidationResult negative = validator.validate(acc, new BigDecimal("-1.00"));

        assertFalse(zero.isValid());
        assertEquals("Invalid transfer amount", zero.getMessage());
        assertFalse(negative.isValid());
        assertEquals("Invalid transfer amount", negative.getMessage());
    }

    @Test
    void validate_exceedsLimit_returnsFailWithMessageAndNoUpdate() {
        BigDecimal limit = new BigDecimal("100.00");
        DailyLimitValidator validator = new DailyLimitValidator(limit);

        Account acc = new Account(UUID.randomUUID(), "acc-3", true, new BigDecimal("10.00"), new BigDecimal("95.00"), "Cliente");

        ValidationResult result = validator.validate(acc, new BigDecimal("10.00"));

        assertFalse(result.isValid());
        String expected = "Daily transfer limit exceeded. Exceeded by 5.00. Remaining 5.00.";
        assertEquals(expected, result.getMessage());
        assertEquals(0, acc.getDailyTransferred().compareTo(new BigDecimal("95.00")));
    }

    @Test
    void validate_withinLimit_updatesDailyTransferred_andDelegatesToNext_whenNextPresent() {
        BigDecimal limit = new BigDecimal("100.00");
        DailyLimitValidator validator = new DailyLimitValidator(limit);
        validator.setNext(next);

        Account acc = new Account(UUID.randomUUID(), "acc-4", true, new BigDecimal("50.00"), new BigDecimal("20.00"), "Cliente");
        when(next.validate(acc, new BigDecimal("10.00"))).thenReturn(ValidationResult.ok());

        ValidationResult result = validator.validate(acc, new BigDecimal("10.00"));

        assertTrue(result.isValid());
        assertEquals(0, acc.getDailyTransferred().compareTo(new BigDecimal("30.00")));
        verify(next, times(1)).validate(acc, new BigDecimal("10.00"));
    }

    @Test
    void validate_withinLimit_withoutNext_returnsOk_andUpdatesDailyTransferred() {
        BigDecimal limit = new BigDecimal("100.00");
        DailyLimitValidator validator = new DailyLimitValidator(limit);

        Account acc = new Account(UUID.randomUUID(), "acc-5", true, new BigDecimal("80.00"), new BigDecimal("0.00"), "Cliente");

        ValidationResult result = validator.validate(acc, new BigDecimal("20.00"));

        assertTrue(result.isValid());
        assertEquals(0, acc.getDailyTransferred().compareTo(new BigDecimal("20.00")));
    }

    @Test
    void setNext_returnsProvidedNext() {
        DailyLimitValidator validator = new DailyLimitValidator(new BigDecimal("100.00"));

        TransferValidator returned = validator.setNext(next);

        assertSame(next, returned);
    }
}
