package com.itau.challenge.bank.infrastructure.provider;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class DailyLimitProviderImplTest {

    @Test
    void getDailyLimit_returnsConfiguredValue() throws Exception {
        DailyLimitProviderImpl provider = new DailyLimitProviderImpl();

        Field field = DailyLimitProviderImpl.class.getDeclaredField("dailyLimit");
        field.setAccessible(true);
        BigDecimal expected = new BigDecimal("1000.00");
        field.set(provider, expected);

        BigDecimal actual = provider.getDailyLimit();
        assertNotNull(actual);
        assertEquals(0, actual.compareTo(expected));
    }

    @Test
    void getDailyLimit_whenNotSet_returnsNull() {
        DailyLimitProviderImpl provider = new DailyLimitProviderImpl();
        assertNull(provider.getDailyLimit());
    }
}
