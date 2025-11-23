package com.itau.challenge.bank.domain.provider;

import com.itau.challenge.bank.domain.entity.QueueMessage;

public interface BacenNotifierProvider {
    boolean notifyBacen(QueueMessage message);
}
