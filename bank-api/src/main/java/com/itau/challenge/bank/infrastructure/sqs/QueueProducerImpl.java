package com.itau.challenge.bank.infrastructure.sqs;

import com.itau.challenge.bank.domain.entity.QueueMessage;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class QueueProducerImpl implements QueueProducer{

    private static final Logger log = LoggerFactory.getLogger(QueueProducerImpl.class);

    private final SqsTemplate sqsTemplate;

    public QueueProducerImpl(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    @Value("${aws.sqs.queue-name}")
    private String queueName;

    @Override
    public void sendMessage(QueueMessage message) {
        log.info("Sending message to SQS: {}", message);
        sqsTemplate.send(to -> to
                .queue(queueName)
                .payload(message)
        );
    }
}
