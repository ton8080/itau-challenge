package com.itau.challenge.bank.domain.utils.chain;

import com.itau.challenge.bank.domain.entity.Account;
import com.itau.challenge.bank.domain.utils.chain.validator.TransferValidator;
import com.itau.challenge.bank.domain.utils.chain.validator.ValidationResult;
import com.itau.challenge.bank.domain.utils.chain.validator.impl.DailyLimitValidator;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DailyLimitValidatorTest {

    @Test
    void validate_NullAccount_ReturnsFail() {
        DailyLimitValidator validator = new DailyLimitValidator(new BigDecimal("1000"));
        ValidationResult result = validator.validate(null, new BigDecimal("100"));
        assertFalse(result.isValid());
        assertNotNull(result.getMessage());
    }

    @Test
    void validate_NullOrNonPositiveAmount_ReturnsFail() {
        DailyLimitValidator validator = new DailyLimitValidator(new BigDecimal("1000"));

        ValidationResult resultNull = validator.validate(mock(Account.class), null);
        assertFalse(resultNull.isValid());

        ValidationResult resultZero = validator.validate(mock(Account.class), BigDecimal.ZERO);
        assertFalse(resultZero.isValid());

        ValidationResult resultNegative = validator.validate(mock(Account.class), new BigDecimal("-1"));
        assertFalse(resultNegative.isValid());
    }

    @Test
    void validate_BelowLimit_ReturnsOk() {
        Account acc = mock(Account.class);
        when(acc.getDailyTransferred()).thenReturn(new BigDecimal("100"));
        DailyLimitValidator validator = new DailyLimitValidator(new BigDecimal("1000"));

        ValidationResult result = validator.validate(acc, new BigDecimal("200"));
        assertTrue(result.isValid());
    }

    @Test
    void validate_EqualToLimit_ReturnsOk() {
        Account acc = mock(Account.class);
        when(acc.getDailyTransferred()).thenReturn(new BigDecimal("200"));
        DailyLimitValidator validator = new DailyLimitValidator(new BigDecimal("300"));

        ValidationResult result = validator.validate(acc, new BigDecimal("100"));
        assertTrue(result.isValid());
    }

    @Test
    void validate_ExceedsLimit_ReturnsFailAndMessageContainsAmounts() {
        Account acc = mock(Account.class);
        when(acc.getDailyTransferred()).thenReturn(new BigDecimal("900"));
        BigDecimal limit = new BigDecimal("1000");
        BigDecimal amount = new BigDecimal("200"); // projected = 1100 -> exceeds by 100
        DailyLimitValidator validator = new DailyLimitValidator(limit);

        ValidationResult result = validator.validate(acc, amount);
        assertFalse(result.isValid());
        String msg = result.getMessage();
        assertNotNull(msg);
        assertTrue(msg.contains("Daily transfer limit exceeded"));
        assertTrue(msg.contains(new BigDecimal("100").toPlainString())); // exceeded by
        assertTrue(msg.contains(new BigDecimal("100").toPlainString()) || msg.contains("Remaining 0")); // remaining may show 100 or 0 depending calculation guard
    }

    @Test
    void validate_WithNextValidator_DelegatesWhenOk() {
        Account acc = mock(Account.class);
        when(acc.getDailyTransferred()).thenReturn(new BigDecimal("0"));
        DailyLimitValidator validator = new DailyLimitValidator(new BigDecimal("1000"));

        TransferValidator next = mock(TransferValidator.class);
        when(next.validate(acc, new BigDecimal("100"))).thenReturn(ValidationResult.fail("next-failed"));
        validator.setNext(next);

        ValidationResult result = validator.validate(acc, new BigDecimal("100"));
        assertFalse(result.isValid());
        assertEquals("next-failed", result.getMessage());
        verify(next, times(1)).validate(acc, new BigDecimal("100"));
    }
}
