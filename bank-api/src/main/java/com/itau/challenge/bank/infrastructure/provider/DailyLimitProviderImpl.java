package com.itau.challenge.bank.infrastructure.provider;

import com.itau.challenge.bank.domain.provider.DailyLimitProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DailyLimitProviderImpl implements DailyLimitProvider {

    @Value("${bank.daily-limit}")
    private BigDecimal dailyLimit;

    @Override
    public BigDecimal getDailyLimit() {
        return dailyLimit;
    }
}