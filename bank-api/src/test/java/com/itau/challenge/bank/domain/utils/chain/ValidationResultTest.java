package com.itau.challenge.bank.domain.utils.chain;

import com.itau.challenge.bank.domain.utils.chain.validator.ValidationResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationResultTest {

    @Test
    void ok_returnsValidAndNullMessage() {
        ValidationResult result = ValidationResult.ok();

        assertTrue(result.isValid(), "ok() should be valid");
        assertNull(result.getMessage(), "ok() should have null message");
    }

    @Test
    void fail_returnsInvalidAndProvidedMessage() {
        String msg = "validation failed";
        ValidationResult result = ValidationResult.fail(msg);

        assertFalse(result.isValid(), "fail(...) should be invalid");
        assertEquals(msg, result.getMessage(), "fail(...) should return the provided message");
    }

    @Test
    void fail_withEmptyMessage_returnsInvalidAndEmptyMessage() {
        String msg = "";
        ValidationResult result = ValidationResult.fail(msg);

        assertFalse(result.isValid());
        assertEquals(msg, result.getMessage());
    }
}
