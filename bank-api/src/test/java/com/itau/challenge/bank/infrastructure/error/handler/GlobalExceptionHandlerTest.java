package com.itau.challenge.bank.infrastructure.error.handler;

import com.itau.challenge.bank.domain.exceptions.AccountNotFoundException;
import com.itau.challenge.bank.domain.exceptions.InvalidAmountException;
import com.itau.challenge.bank.domain.exceptions.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Mock
    private AccountNotFoundException accountNotFoundException;
    @Mock
    private InvalidAmountException invalidAmountException;
    @Mock
    private ValidationException validationException;

    @Test
    void handleAccountNotFound_returnsNotFoundResponse() {
        when(accountNotFoundException.getMessage()).thenReturn("account not found");

        ResponseEntity<Map<String, Object>> response = handler.handleAccountNotFound(accountNotFoundException);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
        assertEquals(404, ((Number) body.get("status")).intValue());
        assertEquals("Not Found", body.get("error"));
        assertEquals("account not found", body.get("message"));
        assertNull(body.get("details"));
    }

    @Test
    void handleInvalidAmount_returnsBadRequestResponse() {
        when(invalidAmountException.getMessage()).thenReturn("invalid amount");

        ResponseEntity<Map<String, Object>> response = handler.handleInvalidAmount(invalidAmountException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
        assertEquals(400, ((Number) body.get("status")).intValue());
        assertEquals("Bad Request", body.get("error"));
        assertEquals("invalid amount", body.get("message"));
        assertNull(body.get("details"));
    }

    @Test
    void handleValidation_returnsBadRequestWithDetails() {
        when(validationException.getMessage()).thenReturn("validation failed");
        when(validationException.getRemainingBalance()).thenReturn(new BigDecimal("12.34"));

        ResponseEntity<Map<String, Object>> response = handler.handleValidation(validationException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
        assertEquals(400, ((Number) body.get("status")).intValue());
        assertEquals("Bad Request", body.get("error"));
        assertEquals("validation failed", body.get("message"));

        Object detailsObj = body.get("details");
        assertNotNull(detailsObj);
        assertTrue(detailsObj instanceof Map);
        @SuppressWarnings("unchecked")
        Map<String, Object> details = (Map<String, Object>) detailsObj;
        assertEquals(new BigDecimal("12.34"), details.get("remainingBalance"));
    }

    @Test
    void handleGeneric_returnsInternalServerError() {
        Exception ex = new RuntimeException("unexpected error");

        ResponseEntity<Map<String, Object>> response = handler.handleGeneric(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
        assertEquals(500, ((Number) body.get("status")).intValue());
        assertEquals("Internal Server Error", body.get("error"));
        assertEquals("unexpected error", body.get("message"));
        assertNull(body.get("details"));
    }
}
