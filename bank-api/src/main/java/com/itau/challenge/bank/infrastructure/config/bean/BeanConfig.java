package com.itau.challenge.bank.infrastructure.config.bean;

import com.itau.challenge.bank.domain.provider.*;
import com.itau.challenge.bank.domain.usecase.CheckAccountUseCase;
import com.itau.challenge.bank.domain.usecase.TransferUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public TransferUseCase transferUseCase(GetCustomerByIdProvider getCustomerByIdProvider,
                                           SaveAccountProvider saveAccountProvider,
                                           FindAccountByIdProvider findAccountByIdProvider,
                                           FindByAccountNumberProvider findByAccountNumberProvider,
                                           BacenNotifierProvider bacenNotifier,
                                           DailyLimitProvider dailyLimitProvider) {
        return new TransferUseCase(getCustomerByIdProvider, saveAccountProvider, findAccountByIdProvider, findByAccountNumberProvider, bacenNotifier, dailyLimitProvider);
    }

    @Bean
    public CheckAccountUseCase checkAccountUseCase(FindAccountByIdProvider findAccountByIdProvider) {
        return new CheckAccountUseCase(findAccountByIdProvider);
    }
}
