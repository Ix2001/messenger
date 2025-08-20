package com.example.messenger.ws;

import com.example.messenger.dto.SendMessageRequest;

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
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Slf4j
@Controller
@RequiredArgsConstructor
@Tag(name = "WebSocket Chat", description = "WebSocket endpoints для чатов")
public class ChatWsController {

    private final SimpMessagingTemplate template;
    private final MessageService messageService;

    /**
     * WebSocket endpoint для отправки сообщений в чат
     * 
     * @param chatId ID чата (UUID)
     * @param request Данные сообщения (текст)
     * @param principal Аутентифицированный пользователь
     * 
     * WebSocket URL: /app/chats/{chatId}/send
     * Subscription Topic: /topic/chat/{chatId}
     * 
     * Пример использования:
     * 1. Подключитесь к WebSocket: ws://localhost:8080/ws
     * 2. Подпишитесь на топик: /topic/chat/{chatId}
     * 3. Отправьте сообщение: /app/chats/{chatId}/send
     */
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


}
