package com.itau.challenge.bank.infrastructure.provider;

import com.itau.challenge.bank.domain.entity.QueueMessage;
import com.itau.challenge.bank.infrastructure.sqs.QueueProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BacenNotifierProviderImplTest {

    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private WebClient.RequestBodySpec requestBodySpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;
    @Mock
    private QueueProducer queueProducer;
    @Mock
    private QueueMessage message;

    private BacenNotifierProviderImpl provider;

    @BeforeEach
    void setUp() throws Exception {
        provider = new BacenNotifierProviderImpl(webClient, queueProducer);

        Field hostField = BacenNotifierProviderImpl.class.getDeclaredField("host");
        hostField.setAccessible(true);
        hostField.set(provider, "http://bacen.api");

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
    }

    @Test
    void notifyBacen_whenRemoteReturnsTrue_shouldReturnTrue_andNotSendToQueue() {
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(Boolean.TRUE));
        when(message.getTransactionId()).thenReturn("tx-1");

        boolean result = provider.notifyBacen(message);

        assertTrue(result);
        verify(queueProducer, never()).sendMessage(any());
        verify(webClient, times(1)).post();
        verify(requestBodyUriSpec, times(1)).uri(endsWith("/notificar"));
        verify(requestBodySpec, times(1)).bodyValue(eq(message));
        verify(requestHeadersSpec, times(1)).retrieve();
    }

    @Test
    void notifyBacen_whenRemoteReturnsNull_shouldReturnFalse_andNotSendToQueue() {
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.empty());
        when(message.getTransactionId()).thenReturn("tx-2");

        boolean result = provider.notifyBacen(message);

        assertFalse(result);
        verify(queueProducer, never()).sendMessage(any());
    }

    @Test
    void fallbackQueue_shouldSendMessageToQueue_andReturnFalse() throws Exception {
        Method fallback = BacenNotifierProviderImpl.class.getDeclaredMethod("fallbackQueue", QueueMessage.class, Throwable.class);
        fallback.setAccessible(true);

        Throwable ex = new RuntimeException("simulated error");
        boolean returned = (boolean) fallback.invoke(provider, message, ex);

        assertFalse(returned);
        verify(queueProducer, times(1)).sendMessage(eq(message));
    }
}
