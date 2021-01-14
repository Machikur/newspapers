package com.papers.integration.newspaper;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
public class LogResponse {

    @Bean
    MessageChannel logChannel() {
        return new DirectChannel();
    }

    @Bean
    IntegrationFlow logResponseFlow() {
        return IntegrationFlows.from("logChannel")
                .log(LoggingHandler.Level.INFO, "Info", m -> "Operacja zakończona powodzeniem")
                .enrichHeaders(c -> c.header(org.springframework.integration.http.HttpHeaders.STATUS_CODE, HttpStatus.OK))
                .get();
    }

}
