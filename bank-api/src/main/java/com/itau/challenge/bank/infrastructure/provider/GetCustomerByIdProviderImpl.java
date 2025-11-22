package com.itau.challenge.bank.infrastructure.provider;

import com.itau.challenge.bank.application.dto.CostumersResponseDTO;
import com.itau.challenge.bank.domain.entity.Account;
import com.itau.challenge.bank.domain.provider.GetCustomerByIdProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GetCustomerByIdProviderImpl implements GetCustomerByIdProvider {

    private final WebClient webClient;

    @Value("${cadastro.api.host}")
    private String host;

    @Value("${cadastro.api.path.customerById}")
    private String path;

    public GetCustomerByIdProviderImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Account getCustomerById(String id) {
        String url = this.host + this.path;
        url = url.replace("{customer_id}", id);
        CostumersResponseDTO result = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(CostumersResponseDTO.class)
                .block();

        return new Account(null, null, result.activeAccount(), null, null, result.name());
    }
}
