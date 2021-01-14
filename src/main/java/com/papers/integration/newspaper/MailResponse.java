package com.papers.integration.newspaper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailResponse {

    @Value("${admin.mail}")
    private String adminMail;

    @Value("${MAIL_USERNAME}")
    private String userName;

    @Value("${MAIL_PASSWORD}")
    private String password;

    @Bean
    MessageChannel mailChannel() {
        return new DirectChannel();
    }

    @Bean
    IntegrationFlow mailResponseFlow() {
        return IntegrationFlows.from("mailChannel")
                .enrichHeaders(Mail.headers()
                        .to(adminMail)
                        .from("Application")
                        .subject("Powiadomienie"))
                .transform(o -> {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setText("Operacja zapisu do bazy danych zakończona");
                    return message;
                })
                .log(LoggingHandler.Level.INFO, "Info", m -> "Wysyłam maila")
                .handle(Mail.outboundAdapter("smtp.gmail.com")
                        .javaMailProperties(p -> p
                                .put("mail.smtp.starttls.enable", "true")
                                .put("mail.smtp.auth", "true"))
                        .port(587)
                        .credentials(userName, password)
                        .protocol("smtp"))
                .get();
    }

}




