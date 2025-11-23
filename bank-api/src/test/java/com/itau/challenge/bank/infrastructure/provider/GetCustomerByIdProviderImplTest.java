package com.itau.challenge.bank.infrastructure.provider;

import com.itau.challenge.bank.application.dto.CostumersResponseDTO;
import com.itau.challenge.bank.domain.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.endsWith;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCustomerByIdProviderImplTest {

    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    private GetCustomerByIdProviderImpl provider;

    @BeforeEach
    void setUp() throws Exception {
        provider = new GetCustomerByIdProviderImpl(webClient);

        Field hostField = GetCustomerByIdProviderImpl.class.getDeclaredField("host");
        hostField.setAccessible(true);
        hostField.set(provider, "http://cadastro.api");

        Field pathField = GetCustomerByIdProviderImpl.class.getDeclaredField("path");
        pathField.setAccessible(true);
        pathField.set(provider, "/customers/{customer_id}");

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void getCustomerById_whenRemoteReturnsDto_shouldReturnAccountWithNameAndActiveFlag() throws Exception {
        CostumersResponseDTO dto = mock(CostumersResponseDTO.class);
        when(dto.activeAccount()).thenReturn(true);
        when(dto.name()).thenReturn("John Doe");

        when(responseSpec.bodyToMono(CostumersResponseDTO.class)).thenReturn(Mono.just(dto));

        Account account = provider.getCustomerById("123");

        assertNotNull(account);

        Field nameField = Account.class.getDeclaredField("customerName");
        nameField.setAccessible(true);
        assertEquals("John Doe", nameField.get(account));

        Field activeField = Account.class.getDeclaredField("activeAccount");
        activeField.setAccessible(true);
        assertEquals(true, activeField.get(account));

        verify(webClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri(endsWith("/customers/123"));
        verify(requestHeadersSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).bodyToMono(CostumersResponseDTO.class);
    }

    @Test
    void getCustomerById_whenRemoteReturnsEmpty_shouldThrowNullPointerException() {
        when(responseSpec.bodyToMono(CostumersResponseDTO.class)).thenReturn(Mono.empty());

        assertThrows(NullPointerException.class, () -> provider.getCustomerById("123"));

        verify(webClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri(endsWith("/customers/123"));
        verify(requestHeadersSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).bodyToMono(CostumersResponseDTO.class);
    }
}
