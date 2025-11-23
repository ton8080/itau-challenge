package com.itau.challenge.bank.domain.utils.chain;

import com.itau.challenge.bank.domain.entity.Account;
import com.itau.challenge.bank.domain.utils.chain.validator.impl.CustomerValidator;
import com.itau.challenge.bank.domain.utils.chain.validator.TransferValidator;
import com.itau.challenge.bank.domain.utils.chain.validator.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerValidatorTest {

    @Mock
    private TransferValidator next;

    @Test
    void returnsFailWhenCustomerNameIsNull() {
        CustomerValidator validator = new CustomerValidator();

        Account acc = new Account(UUID.randomUUID(), "acc-num", true, BigDecimal.ZERO, BigDecimal.ZERO, null);
        ValidationResult result = validator.validate(acc, new BigDecimal("10.00"));

        assertFalse(result.isValid());
        assertEquals("Customer not found", result.getMessage());
    }

    @Test
    void returnsOkWhenCustomerNamePresentAndNoNext() {
        CustomerValidator validator = new CustomerValidator();

        Account acc = new Account(UUID.randomUUID(), "acc-num", true, BigDecimal.ZERO, BigDecimal.ZERO, "Joao Silva");

        ValidationResult result = validator.validate(acc, new BigDecimal("1.00"));

        assertTrue(result.isValid());
        assertNull(result.getMessage());
    }

    @Test
    void delegatesToNextWhenCustomerNamePresentAndNextReturnsFail() {
        CustomerValidator validator = new CustomerValidator();
        validator.setNext(next);

        Account acc = new Account(UUID.randomUUID(), "acc-num", true, BigDecimal.ZERO, BigDecimal.ZERO, "Joao Silva");
        BigDecimal amount = new BigDecimal("5.00");

        when(next.validate(acc, amount)).thenReturn(ValidationResult.fail("next failed"));

        ValidationResult result = validator.validate(acc, amount);

        assertFalse(result.isValid());
        assertEquals("next failed", result.getMessage());
        verify(next, times(1)).validate(acc, amount);
    }

    @Test
    void delegatesToNextAndReturnsOkWhenNextOk() {
        CustomerValidator validator = new CustomerValidator();
        validator.setNext(next);

        Account acc = new Account(UUID.randomUUID(), "acc-num", true, BigDecimal.ZERO, BigDecimal.ZERO, "Joao Silva");
        BigDecimal amount = new BigDecimal("2.00");

        when(next.validate(acc, amount)).thenReturn(ValidationResult.ok());

        ValidationResult result = validator.validate(acc, amount);

        assertTrue(result.isValid());
        verify(next, times(1)).validate(acc, amount);
    }
}
