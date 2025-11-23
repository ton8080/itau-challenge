package com.itau.challenge.bank.infrastructure.config.sqs;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.net.URI;

@Configuration
public class SqsConfig {

    @Value("${spring.cloud.aws.sqs.endpoint}")
    private String sqsEndpoint;

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                // se estiver usando LocalStack ou endpoint customizado
                .endpointOverride(URI.create(sqsEndpoint))
                .region(software.amazon.awssdk.regions.Region.of("sa-east-1"))
                .build();
    }

    @Bean
    public SqsTemplate sqsTemplate(SqsAsyncClient sqsAsyncClient) {
        return SqsTemplate.builder()
                .sqsAsyncClient(sqsAsyncClient)
                .build();
    }
}
