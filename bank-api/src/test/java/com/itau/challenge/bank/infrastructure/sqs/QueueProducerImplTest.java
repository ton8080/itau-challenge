package com.itau.challenge.bank.infrastructure.sqs;

import com.itau.challenge.bank.domain.entity.QueueMessage;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QueueProducerImplTest {

    @Mock
    private SqsTemplate sqsTemplate;

    private QueueProducerImpl producer;

    @BeforeEach
    void setUp() throws Exception {
        producer = new QueueProducerImpl(sqsTemplate);

        Field queueField = QueueProducerImpl.class.getDeclaredField("queueName");
        queueField.setAccessible(true);
        queueField.set(producer, "test-queue");
    }

    @Test
    void sendMessage_shouldCallSqsTemplateSend() {
        QueueMessage message = mock(QueueMessage.class);

        producer.sendMessage(message);

        verify(sqsTemplate, times(1)).send(any());
    }

    @Test
    void sendMessage_shouldPassLambdaToSqsTemplate() {
        QueueMessage message = mock(QueueMessage.class);

        producer.sendMessage(message);

        ArgumentCaptor<Consumer> captor = ArgumentCaptor.forClass(Consumer.class);
        verify(sqsTemplate).send(captor.capture());

        Consumer captured = captor.getValue();
        assertNotNull(captured, "Expected a non-null consumer to be passed to SqsTemplate.send");
    }

    @Test
    void sendMessage_withNullMessage_shouldStillCallSqsTemplate() {
        producer.sendMessage(null);

        verify(sqsTemplate, times(1)).send(any());
    }
}
