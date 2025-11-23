package com.itau.challenge.bank.domain.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransferTest {

    @Test
    void constructorSetsZeroWhenRemainingBalanceIsNull() {
        Transfer t = new Transfer(true, "ok", false, null);

        assertTrue(t.isSuccess());
        assertEquals("ok", t.getMessage());
        assertFalse(t.isBacenNotified());
        assertNotNull(t.getRemainingBalance());
        assertEquals(0, t.getRemainingBalance().compareTo(BigDecimal.ZERO));
    }

    @Test
    void constructorPreservesProvidedRemainingBalance() {
        BigDecimal remaining = new BigDecimal("123.45");
        Transfer t = new Transfer(false, null, true, remaining);

        assertFalse(t.isSuccess());
        assertNull(t.getMessage());
        assertTrue(t.isBacenNotified());
        assertNotNull(t.getRemainingBalance());
        assertEquals(0, remaining.compareTo(t.getRemainingBalance()));
    }

    @Test
    void settersAndGettersWork() {
        Transfer t = new Transfer(false, "init", false, BigDecimal.ZERO);

        t.setSuccess(true);
        t.setMessage("done");
        t.setBacenNotified(true);
        t.setRemainingBalance(new BigDecimal("10.00"));

        assertTrue(t.isSuccess());
        assertEquals("done", t.getMessage());
        assertTrue(t.isBacenNotified());
        assertEquals(0, new BigDecimal("10.00").compareTo(t.getRemainingBalance()));
    }

    @Test
    void setRemainingBalanceAllowsNull() {
        Transfer t = new Transfer(true, "m", false, BigDecimal.ONE);

        t.setRemainingBalance(null);
        assertNull(t.getRemainingBalance());
    }
}
