package com.itau.challenge.bank.application.controller;

import com.itau.challenge.bank.application.dto.CheckAccountResponseDTO;
import com.itau.challenge.bank.application.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;
    @Mock
    private CheckAccountResponseDTO dto;
    @InjectMocks
    private AccountController controller;


    @Test
    void returnsOkAndBodyWhenAccountExists() {
        UUID accountId = UUID.randomUUID();

        Mockito.when(accountService.checkAccount(accountId)).thenReturn(dto);

        ResponseEntity<CheckAccountResponseDTO> response = controller.checkAccount(accountId);

        assertEquals(200, response.getStatusCodeValue());
        assertSame(dto, response.getBody());
    }

    @Test
    void returnsOkWithNullBodyWhenServiceReturnsNull() {
        UUID accountId = UUID.randomUUID();

        Mockito.when(accountService.checkAccount(accountId)).thenReturn(null);

        ResponseEntity<CheckAccountResponseDTO> response = controller.checkAccount(accountId);

        assertEquals(200, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void throwsExceptionWhenServiceThrowsRuntimeException() {
        UUID accountId = UUID.randomUUID();

        Mockito.when(accountService.checkAccount(accountId)).thenThrow(new IllegalStateException("service failure"));

        assertThrows(IllegalStateException.class, () -> controller.checkAccount(accountId));
    }
}
