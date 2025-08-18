package com.example.messenger.ws;

import com.example.messenger.ws.events.MessageReadEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class WsChatEventsPublisher {

    private final SimpMessagingTemplate template;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMessageRead(MessageReadEvent e) {
        Map<String, Object> payload = Map.of(
                "type", "MESSAGE_READ",
                "chatId", e.chatId().toString(),
                "messageId", e.messageId().toString(),
                "readerId", e.readerId().toString(),
                "readAt", e.readAt().toString()
        );

        template.convertAndSend("/topic/chat/" + e.chatId(), payload);
    }
}
