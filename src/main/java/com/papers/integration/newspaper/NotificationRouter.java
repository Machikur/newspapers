package com.papers.integration.newspaper;

import lombok.RequiredArgsConstructor;
import org.springframework.integration.router.AbstractMessageRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

import static com.papers.integration.newspaper.Headers.RESPONSE_HEADER;

@Component
@RequiredArgsConstructor
public class NotificationRouter extends AbstractMessageRouter {

    private final MessageChannel mailChannel;
    private final MessageChannel logChannel;

    @Override
    protected Collection<MessageChannel> determineTargetChannels(Message<?> message) {
        String notifyType = (String) message.getHeaders().get(RESPONSE_HEADER);
        if (notifyType != null && notifyType.equals("mail")) {
            return Collections.singleton(mailChannel);
        }
        return Collections.singleton(logChannel);
    }

}
