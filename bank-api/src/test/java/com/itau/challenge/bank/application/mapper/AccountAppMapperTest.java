package com.itau.challenge.bank.application.mapper;

import com.itau.challenge.bank.application.dto.CheckAccountResponseDTO;
import com.itau.challenge.bank.domain.entity.Account;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountAppMapperTest {

    private final AccountAppMapper mapper = new AccountAppMapper();

    @Test
    void mapsAllFieldsWhenAccountIsPopulated() {
        Account account = mock(Account.class);
        when(account.getAccountNumber()).thenReturn("123456");
        when(account.isActiveAccount()).thenReturn(true);
        BigDecimal balance = new BigDecimal("1000.50");
        BigDecimal daily = new BigDecimal("250.00");
        when(account.getBalance()).thenReturn(balance);
        when(account.getDailyTransferred()).thenReturn(daily);
        when(account.getCustomerName()).thenReturn("Joao Silva");

        CheckAccountResponseDTO dto = mapper.toDto(account);

        assertNotNull(dto);
        assertEquals("123456", dto.accountNumber());
        assertTrue(dto.activeAccount());
        assertEquals(balance, dto.balance());
        assertEquals(daily, dto.dailyTransferred());
        assertEquals("Joao Silva", dto.customerName());
    }

    @Test
    void returnsNullWhenAccountIsNull() {
        CheckAccountResponseDTO dto = mapper.toDto(null);
        assertNull(dto);
    }

    @Test
    void mapsFalseAndZeroValuesCorrectly() {
        Account account = mock(Account.class);
        when(account.getAccountNumber()).thenReturn("000");
        when(account.isActiveAccount()).thenReturn(false);
        BigDecimal zero = BigDecimal.ZERO;
        when(account.getBalance()).thenReturn(zero);
        when(account.getDailyTransferred()).thenReturn(zero);
        when(account.getCustomerName()).thenReturn(null);

        CheckAccountResponseDTO dto = mapper.toDto(account);

        assertNotNull(dto);
        assertEquals("000", dto.accountNumber());
        assertFalse(dto.activeAccount());
        assertEquals(zero, dto.balance());
        assertEquals(zero, dto.dailyTransferred());
        assertNull(dto.customerName());
    }
}
