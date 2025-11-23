package com.itau.challenge.bank.infrastructure.provider;

import com.itau.challenge.bank.domain.provider.BacenNotifierProvider;
import com.itau.challenge.bank.domain.entity.QueueMessage;
import com.itau.challenge.bank.infrastructure.sqs.QueueProducer;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class BacenNotifierProviderImpl implements BacenNotifierProvider {

    private static final Logger log = LoggerFactory.getLogger(BacenNotifierProviderImpl.class);

    private final WebClient webClient;
    private final QueueProducer queueProducer;

    @Value("${bacen.api.host}")
    private String host;

    public BacenNotifierProviderImpl(WebClient webClient, QueueProducer queueProducer) {
        this.webClient = webClient;
        this.queueProducer = queueProducer;
    }

    @Override
    @Retry(name = "bacenRetry")
    @CircuitBreaker(name = "bacenCircuitBreaker", fallbackMethod = "fallbackQueue")
    public boolean notifyBacen(QueueMessage message) {

        log.info("[BACEN] Sending synchronous notification {}", message.getTransactionId());

        Boolean result = webClient.post()
                .uri(host + "/notificar")
                .bodyValue(message)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> Mono.error(new RuntimeException("Error: " + clientResponse.statusCode()))
                )
                .bodyToMono(Boolean.class)
                .block();

        return result != null && result;
    }

    private boolean fallbackQueue(QueueMessage message, Throwable ex) {

        log.warn("[BACEN] Fallback triggered. Sending to SQS. Reason={}", ex.getMessage());
        queueProducer.sendMessage(message);

        return false;
    }
}
