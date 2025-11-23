package com.itau.challenge.bank.infrastructure.sqs;

import com.itau.challenge.bank.domain.entity.QueueMessage;

public interface QueueProducer {
    void sendMessage(QueueMessage message);
}
