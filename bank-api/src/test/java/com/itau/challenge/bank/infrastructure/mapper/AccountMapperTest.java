package com.itau.challenge.bank.infrastructure.mapper;

import com.itau.challenge.bank.domain.entity.Account;
import com.itau.challenge.bank.infrastructure.persistence.model.AccountModel;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountMapperTest {

    private final AccountMapper mapper = new AccountMapper();

    @Test
    void toDomain_nullInput_returnsNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toDomain_mapsAllFields_andDefaultsNullsToZero() {
        UUID id = UUID.randomUUID();
        AccountModel model = new AccountModel();
        model.setId(id);
        model.setAccountNumber("123456");
        model.setActiveAccount(true);
        model.setBalance(null);
        model.setDailyTransferred(null);
        model.setCustomerName("Joao Silva");
        LocalDate lastReset = LocalDate.of(2025, 1, 1);
        model.setLastDailyReset(lastReset);

        Account domain = mapper.toDomain(model);

        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals("123456", domain.getAccountNumber());
        assertTrue(domain.isActiveAccount());
        assertEquals(0, domain.getBalance().compareTo(BigDecimal.ZERO));
        assertEquals(0, domain.getDailyTransferred().compareTo(BigDecimal.ZERO));
        assertEquals("Joao Silva", domain.getCustomerName());
        assertEquals(lastReset, domain.getLastDailyReset());
    }

    @Test
    void toModel_nullInput_returnsNull() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void toModel_mapsAllFields_andDefaultsNulls_andSetsLastDailyResetToNowWhenNull() {
        UUID id = UUID.randomUUID();
        Account account = new Account();
        account.setId(id);
        account.setAccountNumber("654321");
        account.setActiveAccount(false);
        account.setBalance(null);
        account.setDailyTransferred(null);
        account.setCustomerName("Maria Silva");
        account.setLastDailyReset(null);

        AccountModel model = mapper.toModel(account);

        assertNotNull(model);
        assertEquals(id, model.getId());
        assertEquals("654321", model.getAccountNumber());
        assertFalse(model.isActiveAccount());
        assertEquals(0, model.getBalance().compareTo(BigDecimal.ZERO));
        assertEquals(0, model.getDailyTransferred().compareTo(BigDecimal.ZERO));
        assertEquals("Maria Silva", model.getCustomerName());
        assertNotNull(model.getLastDailyReset());
        assertEquals(LocalDate.now(), model.getLastDailyReset());
    }
}
