package com.example.messenger.ws;

import com.example.messenger.dto.SendMessageRequest;
import com.example.messenger.dto.SendMessageWithFilesRequest;
import com.example.messenger.service.MessageService;
import com.example.messenger.util.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatWsController {

    private final SimpMessagingTemplate template;
    private final MessageService messageService;

    // клиент шлёт в: /app/chats/{chatId}/send
    @MessageMapping("/chats/{chatId}/send")
    public void sendToChat(
            @DestinationVariable("chatId") String chatId,
            @Payload SendMessageRequest request,
            Principal principal
    ) {
        String sender = principal != null ? principal.getName() : "anonymous";
        String text = request != null ? request.text() : null;

        log.debug("WS SEND chatId={}, sender={}, text={}", chatId, sender, text);
        var message = messageService.send(UUID.fromString(chatId), sender, text);
        var messageDto = Mapper.toDto(message);
        
        // публикуем всем подписчикам /topic/chat/{chatId}
        template.convertAndSend("/topic/chat/" + chatId, messageDto);
    }

    // клиент шлёт в: /app/chats/{chatId}/send-with-files
    @MessageMapping("/chats/{chatId}/send-with-files")
    public void sendToChatWithFiles(
            @DestinationVariable("chatId") String chatId,
            @Payload SendMessageWithFilesRequest request,
            Principal principal
    ) {
        String sender = principal != null ? principal.getName() : "anonymous";
        String text = request != null ? request.getText() : null;
        var fileIds = request != null ? request.getFileIds() : null;

        log.debug("WS SEND WITH FILES chatId={}, sender={}, text={}, fileIds={}", chatId, sender, text, fileIds);
        var message = messageService.sendWithFiles(UUID.fromString(chatId), sender, text, fileIds);
        var messageDto = Mapper.toDto(message);
        
        // публикуем всем подписчикам /topic/chat/{chatId}
        template.convertAndSend("/topic/chat/" + chatId, messageDto);
    }
}
