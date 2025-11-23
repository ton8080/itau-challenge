package com.itau.challenge.bank.application.service;

import com.itau.challenge.bank.application.dto.TransferRequestDTO;
import com.itau.challenge.bank.application.dto.TransferResponseDTO;
import com.itau.challenge.bank.application.mapper.TransferMapper;
import com.itau.challenge.bank.domain.entity.Transfer;
import com.itau.challenge.bank.domain.usecase.TransferUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private TransferUseCase transferUseCase;

    @Mock
    private TransferMapper transferMapper;

    @InjectMocks
    private TransferService service;

    @Test
    void returnsDtoWhenTransferSucceeds() {
        TransferRequestDTO request = mock(TransferRequestDTO.class);
        String fromId = "from-id";
        BigDecimal amount = new BigDecimal("100.00");
        String toAccount = "to-account";

        when(request.fromAccountId()).thenReturn(fromId);
        when(request.amount()).thenReturn(amount);
        when(request.toAccountNumber()).thenReturn(toAccount);

        Transfer domain = mock(Transfer.class);
        TransferResponseDTO dto = mock(TransferResponseDTO.class);

        when(transferUseCase.execute(fromId, amount, toAccount)).thenReturn(domain);
        when(transferMapper.toDto(domain)).thenReturn(dto);

        TransferResponseDTO result = service.transfer(request);

        assertSame(dto, result);
        verify(transferUseCase).execute(fromId, amount, toAccount);
        verify(transferMapper).toDto(domain);
    }

    @Test
    void throwsNullPointerWhenRequestIsNull() {
        assertThrows(RuntimeException.class, () -> service.transfer(null));
    }
}
