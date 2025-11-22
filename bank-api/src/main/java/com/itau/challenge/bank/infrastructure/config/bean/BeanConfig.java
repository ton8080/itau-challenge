package com.itau.challenge.bank.infrastructure.config.bean;

import com.itau.challenge.bank.domain.provider.GetCustomerByIdProvider;
import com.itau.challenge.bank.domain.usecase.TransferUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public TransferUseCase transferUseCase(GetCustomerByIdProvider getCustomerByIdProvider) {
        return new TransferUseCase(getCustomerByIdProvider);
    }
}
