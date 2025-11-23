package com.itau.challenge.bank.domain.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void constructorSetsZeroWhenBalanceAndDailyTransferredAreNull() {
        UUID id = UUID.randomUUID();
        Account account = new Account(id, "123", true, null, null, "João Silva");

        assertNotNull(account.getBalance());
        assertNotNull(account.getDailyTransferred());
        assertEquals(0, account.getBalance().compareTo(BigDecimal.ZERO));
        assertEquals(0, account.getDailyTransferred().compareTo(BigDecimal.ZERO));
        assertEquals("123", account.getAccountNumber());
        assertTrue(account.isActiveAccount());
        assertEquals("João Silva", account.getCustomerName());
    }

    @Test
    void debitReturnsNewAccountWithDecreasedBalanceAndIncreasedDailyTransferred() {
        Account original = new Account(UUID.randomUUID(), "acc-1", true,
                new BigDecimal("100.00"), new BigDecimal("10.00"), "João Silva");

        Account after = original.debit(new BigDecimal("30.00"));

        assertEquals(0, original.getBalance().compareTo(new BigDecimal("100.00")));
        assertEquals(0, original.getDailyTransferred().compareTo(new BigDecimal("10.00")));

        assertEquals(0, after.getBalance().compareTo(new BigDecimal("70.00")));
        assertEquals(0, after.getDailyTransferred().compareTo(new BigDecimal("40.00")));
        assertEquals(original.getId(), after.getId());
        assertEquals(original.getAccountNumber(), after.getAccountNumber());
    }

    @Test
    void creditReturnsNewAccountWithIncreasedBalance() {
        Account original = new Account(UUID.randomUUID(), "acc-2", true,
                new BigDecimal("50.00"), BigDecimal.ZERO, "João Silva");

        Account after = original.credit(new BigDecimal("25.00"));

        assertEquals(0, original.getBalance().compareTo(new BigDecimal("50.00")));

        assertEquals(0, after.getBalance().compareTo(new BigDecimal("75.00")));
        assertEquals(0, after.getDailyTransferred().compareTo(BigDecimal.ZERO));
    }

    @Test
    void gettersAndSettersWork() {
        Account a = new Account();
        UUID id = UUID.randomUUID();
        a.setId(id);
        a.setAccountNumber("acc-3");
        a.setActiveAccount(false);
        a.setBalance(new BigDecimal("10.00"));
        a.setDailyTransferred(new BigDecimal("1.00"));
        a.setCustomerName("João Silva");
        LocalDate date = LocalDate.now();
        a.setLastDailyReset(date);

        assertEquals(id, a.getId());
        assertEquals("acc-3", a.getAccountNumber());
        assertFalse(a.isActiveAccount());
        assertEquals(0, a.getBalance().compareTo(new BigDecimal("10.00")));
        assertEquals(0, a.getDailyTransferred().compareTo(new BigDecimal("1.00")));
        assertEquals("João Silva", a.getCustomerName());
        assertEquals(date, a.getLastDailyReset());
    }

    @Test
    void toStringIncludesKeyFields() {
        Account a = new Account(UUID.randomUUID(), "acct-99", true,
                new BigDecimal("5.00"), BigDecimal.ZERO, "João Silva");
        String s = a.toString();

        assertTrue(s.contains("acct-99"));
        assertTrue(s.contains("João Silva"));
        assertTrue(s.contains("balance=5.00") || s.contains("balance=5"));
    }
}
