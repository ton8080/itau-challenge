package com.itau.challenge.bank.infrastructure.config.client;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        TcpClient tcpClient = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 50)
                .doOnConnected(conn -> {
                    conn.addHandlerLast(new ReadTimeoutHandler(50, TimeUnit.MILLISECONDS));
                    conn.addHandlerLast(new WriteTimeoutHandler(50, TimeUnit.MILLISECONDS));
                });

        HttpClient httpClient = HttpClient.from(tcpClient);

        return builder
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
