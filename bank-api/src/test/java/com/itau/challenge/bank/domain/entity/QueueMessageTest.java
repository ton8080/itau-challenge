package com.itau.challenge.bank.domain.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class QueueMessageTest {

    @Test
    void constructorSetsFieldsAndGeneratesIsoTimestamp() {
        BigDecimal amount = new BigDecimal("123.45");
        QueueMessage msg = new QueueMessage("tx-1", "from-acc", "to-acc", amount);

        assertEquals("tx-1", msg.getTransactionId());
        assertEquals("from-acc", msg.getFromAccount());
        assertEquals("to-acc", msg.getToAccount());
        assertNotNull(msg.getAmount());
        assertEquals(0, msg.getAmount().compareTo(amount));
        assertNotNull(msg.getTimestamp());

        assertDoesNotThrow(() -> Instant.parse(msg.getTimestamp()));
    }

    @Test
    void settersUpdateFieldsAndTimestampCanBeOverwritten() {
        QueueMessage msg = new QueueMessage("tx-2", "a", "b", new BigDecimal("1.00"));

        msg.setTransactionId("tx-2-upd");
        msg.setFromAccount("from-upd");
        msg.setToAccount("to-upd");
        msg.setAmount(new BigDecimal("9.99"));
        String customTs = "2020-01-01T00:00:00Z";
        msg.setTimestamp(customTs);

        assertEquals("tx-2-upd", msg.getTransactionId());
        assertEquals("from-upd", msg.getFromAccount());
        assertEquals("to-upd", msg.getToAccount());
        assertEquals(0, msg.getAmount().compareTo(new BigDecimal("9.99")));
        assertEquals(customTs, msg.getTimestamp());
        assertDoesNotThrow(() -> Instant.parse(msg.getTimestamp()));
    }

    @Test
    void allowsNullAmountInConstructorAndSetter() {
        QueueMessage msg = new QueueMessage("tx-3", "x", "y", null);
        assertNull(msg.getAmount());

        msg.setAmount(new BigDecimal("0.00"));
        assertNotNull(msg.getAmount());
        assertEquals(0, msg.getAmount().compareTo(BigDecimal.ZERO));

        msg.setAmount(null);
        assertNull(msg.getAmount());
    }
}
