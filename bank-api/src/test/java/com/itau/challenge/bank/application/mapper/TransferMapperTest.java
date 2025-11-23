package com.itau.challenge.bank.application.mapper;

import com.itau.challenge.bank.application.dto.TransferResponseDTO;
import com.itau.challenge.bank.domain.entity.Transfer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferMapperTest {

    private final TransferMapper mapper = new TransferMapper();

    @Test
    void mapsAllFieldsWhenTransferIsPopulated() {
        Transfer transfer = mock(Transfer.class);
        when(transfer.isSuccess()).thenReturn(true);
        when(transfer.getMessage()).thenReturn("Transferência realizada");
        when(transfer.isBacenNotified()).thenReturn(false);
        BigDecimal remaining = new BigDecimal("123.45");
        when(transfer.getRemainingBalance()).thenReturn(remaining);

        TransferResponseDTO dto = mapper.toDto(transfer);

        assertNotNull(dto);
        assertTrue(dto.success());
        assertEquals("Transferência realizada", dto.message());
        assertFalse(dto.bacenNotified());
        assertEquals(remaining, dto.remainingBalance());
    }

    @Test
    void returnsNullWhenTransferIsNull() {
        assertNull(mapper.toDto(null));
    }

    @Test
    void mapsFalseAndNullMessageAndZeroBalanceCorrectly() {
        Transfer transfer = mock(Transfer.class);
        when(transfer.isSuccess()).thenReturn(false);
        when(transfer.getMessage()).thenReturn(null);
        when(transfer.isBacenNotified()).thenReturn(true);
        when(transfer.getRemainingBalance()).thenReturn(BigDecimal.ZERO);

        TransferResponseDTO dto = mapper.toDto(transfer);

        assertNotNull(dto);
        assertFalse(dto.success());
        assertNull(dto.message());
        assertTrue(dto.bacenNotified());
        assertEquals(BigDecimal.ZERO, dto.remainingBalance());
    }
}
