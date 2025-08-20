package com.example.messenger.controller;

import com.example.messenger.dto.SendMessageRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/websocket")
@Tag(name = "WebSocket API", description = "WebSocket endpoints для мессенджера")
public class WebSocketApiController {

    @Operation(
            summary = "Отправить сообщение в чат",
            description = "WebSocket endpoint: /app/chats/{chatId}/send\n" +
                    "Подписка на сообщения: /topic/chat/{chatId}\n" +
                    "Требует аутентификации через JWT токен в заголовке STOMP"
    )
    @PostMapping("/chats/{chatId}/send")
    public ResponseEntity<Map<String, Object>> sendMessageExample(
            @Parameter(description = "ID чата", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String chatId,
            @Parameter(description = "Данные сообщения", required = true)
            @RequestBody SendMessageRequest request
    ) {
        return ResponseEntity.ok(Map.of(
                "message", "Это пример для документации. Реальный endpoint работает через WebSocket",
                "websocketEndpoint", "/app/chats/" + chatId + "/send",
                "subscriptionTopic", "/topic/chat/" + chatId,
                "request", request
        ));
    }

    @Operation(
            summary = "Подписаться на чат",
            description = "WebSocket subscription: /topic/chat/{chatId}\n" +
                    "Получает все сообщения в реальном времени для указанного чата"
    )
    @GetMapping("/chats/{chatId}/subscribe")
    public ResponseEntity<Map<String, Object>> subscribeToChatExample(
            @Parameter(description = "ID чата", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String chatId
    ) {
        return ResponseEntity.ok(Map.of(
                "message", "Это пример для документации. Реальная подписка работает через WebSocket",
                "subscriptionTopic", "/topic/chat/" + chatId,
                "description", "Подпишитесь на этот топик для получения сообщений в реальном времени"
        ));
    }

    @Operation(
            summary = "WebSocket соединение",
            description = "WebSocket URL: ws://localhost:8080/ws\n" +
                    "STOMP endpoint: /app\n" +
                    "Требует JWT токен в заголовке STOMP для аутентификации"
    )
    @GetMapping("/connection")
    public ResponseEntity<Map<String, Object>> getConnectionInfo() {
        return ResponseEntity.ok(Map.of(
                "websocketUrl", "ws://localhost:8080/ws",
                "stompEndpoint", "/app",
                "authentication", "JWT токен в STOMP заголовке",
                "protocol", "STOMP over WebSocket"
        ));
    }
}
