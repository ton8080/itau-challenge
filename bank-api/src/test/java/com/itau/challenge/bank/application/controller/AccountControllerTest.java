package com.itau.challenge.bank.application.controller;

import com.itau.challenge.bank.application.dto.CheckAccountResponseDTO;
import com.itau.challenge.bank.application.service.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountControllerTest {

    @Test
    void returnsOkAndBodyWhenAccountExists() {
        AccountService accountService = Mockito.mock(AccountService.class);
        AccountController controller = new AccountController(accountService);
        UUID accountId = UUID.randomUUID();
        CheckAccountResponseDTO dto = Mockito.mock(CheckAccountResponseDTO.class);

        Mockito.when(accountService.checkAccount(accountId)).thenReturn(dto);

        ResponseEntity<CheckAccountResponseDTO> response = controller.checkAccount(accountId);

        assertEquals(200, response.getStatusCodeValue());
        assertSame(dto, response.getBody());
    }

    @Test
    void returnsOkWithNullBodyWhenServiceReturnsNull() {
        AccountService accountService = Mockito.mock(AccountService.class);
        AccountController controller = new AccountController(accountService);
        UUID accountId = UUID.randomUUID();

        Mockito.when(accountService.checkAccount(accountId)).thenReturn(null);

        ResponseEntity<CheckAccountResponseDTO> response = controller.checkAccount(accountId);

        assertEquals(200, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void throwsExceptionWhenServiceThrowsRuntimeException() {
        AccountService accountService = Mockito.mock(AccountService.class);
        AccountController controller = new AccountController(accountService);
        UUID accountId = UUID.randomUUID();

        Mockito.when(accountService.checkAccount(accountId)).thenThrow(new IllegalStateException("service failure"));

        assertThrows(IllegalStateException.class, () -> controller.checkAccount(accountId));
    }
}
