package com.itau.challenge.bank.domain.provider;

import java.math.BigDecimal;

public interface DailyLimitProvider {
    BigDecimal getDailyLimit();
}
