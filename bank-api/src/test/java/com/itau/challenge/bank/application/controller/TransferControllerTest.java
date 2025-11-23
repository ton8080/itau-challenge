package com.itau.challenge.bank.application.controller;

import com.itau.challenge.bank.application.dto.TransferRequestDTO;
import com.itau.challenge.bank.application.dto.TransferResponseDTO;
import com.itau.challenge.bank.application.service.TransferService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferControllerTest {

    @Mock
    private TransferService service;

    @Mock
    private TransferRequestDTO request;

    @Mock
    private TransferResponseDTO response;

    @InjectMocks
    private TransferController controller;

    @Test
    void returns200AndBodyWhenTransferSucceeds() {
        when(service.transfer(request)).thenReturn(response);

        ResponseEntity<TransferResponseDTO> result = controller.transfer(request);

        assertEquals(200, result.getStatusCodeValue());
        assertSame(response, result.getBody());
        verify(service).transfer(request);
    }

    @Test
    void returns200AndNullBodyWhenServiceReturnsNull() {
        when(service.transfer(request)).thenReturn(null);

        ResponseEntity<TransferResponseDTO> result = controller.transfer(request);

        assertEquals(200, result.getStatusCodeValue());
        assertNull(result.getBody());
        verify(service).transfer(request);
    }

    @Test
    void propagatesExceptionWhenServiceThrows() {
        when(service.transfer(request)).thenThrow(new IllegalArgumentException("invalid"));

        assertThrows(IllegalArgumentException.class, () -> controller.transfer(request));
        verify(service).transfer(request);
    }
}
