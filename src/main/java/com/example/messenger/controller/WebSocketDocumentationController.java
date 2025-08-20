package com.example.messenger.controller;

import com.example.messenger.dto.SendMessageRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/websocket-docs")
@Tag(name = "WebSocket Documentation", description = "Документация и примеры WebSocket API")
public class WebSocketDocumentationController {

    @Operation(
            summary = "Получить информацию о WebSocket API",
            description = "Возвращает полную информацию о WebSocket endpoints, примерах использования и инструкциях"
    )
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getWebSocketInfo() {
        return ResponseEntity.ok(Map.of(
                "title", "Messenger WebSocket API",
                "version", "1.1.0",
                "description", "WebSocket API для мессенджера с поддержкой STOMP протокола",
                "websocketUrl", "ws://localhost:8080/ws",
                "authentication", "JWT Bearer Token в STOMP заголовке",
                "protocol", "STOMP over WebSocket",
                "endpoints", Map.of(
                        "sendMessage", Map.of(
                                "url", "/app/chats/{chatId}/send",
                                "method", "SEND",
                                "description", "Отправка сообщения в чат",
                                "payload", "SendMessageRequest"
                        ),
                        "subscribeToChat", Map.of(
                                "url", "/topic/chat/{chatId}",
                                "method", "SUBSCRIBE",
                                "description", "Подписка на сообщения чата",
                                "response", "ChatMessage"
                        )
                )
        ));
    }

    @Operation(
            summary = "Пример отправки сообщения",
            description = "Демонстрирует структуру данных для отправки сообщения через WebSocket"
    )
    @PostMapping("/example/send-message")
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
                "request", request,
                "exampleUsage", Map.of(
                        "javascript", "const socket = new WebSocket('ws://localhost:8080/ws');\n" +
                                "const stompClient = Stomp.over(socket);\n\n" +
                                "stompClient.connect(\n" +
                                "    { 'Authorization': 'Bearer YOUR_JWT_TOKEN' },\n" +
                                "    function (frame) {\n" +
                                "        // Отправка сообщения\n" +
                                "        stompClient.send('/app/chats/" + chatId + "/send', {}, JSON.stringify({\n" +
                                "            text: '" + request.text() + "'\n" +
                                "        }));\n" +
                                "    }\n" +
                                ");",
                        "curl", "curl -X POST http://localhost:8080/api/websocket-docs/example/send-message/" + chatId + " -H 'Content-Type: application/json' -d '{\"text\":\"" + request.text() + "\"}'"
                )
        ));
    }

    @Operation(
            summary = "Пример подписки на чат",
            description = "Демонстрирует как подписаться на сообщения чата"
    )
    @GetMapping("/example/subscribe/{chatId}")
    public ResponseEntity<Map<String, Object>> subscribeExample(
            @Parameter(description = "ID чата", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String chatId
    ) {
        return ResponseEntity.ok(Map.of(
                "message", "Это пример для документации. Реальная подписка работает через WebSocket",
                "subscriptionTopic", "/topic/chat/" + chatId,
                "description", "Подпишитесь на этот топик для получения сообщений в реальном времени",
                "exampleUsage", Map.of(
                        "javascript", "const socket = new WebSocket('ws://localhost:8080/ws');\n" +
                                "const stompClient = Stomp.over(socket);\n\n" +
                                "stompClient.connect(\n" +
                                "    { 'Authorization': 'Bearer YOUR_JWT_TOKEN' },\n" +
                                "    function (frame) {\n" +
                                "        // Подписка на сообщения\n" +
                                "        stompClient.subscribe('/topic/chat/" + chatId + "', function (message) {\n" +
                                "            const messageData = JSON.parse(message.body);\n" +
                                "            console.log('Получено сообщение:', messageData);\n" +
                                "        });\n" +
                                "    }\n" +
                                ");",
                        "expectedResponse", Map.of(
                                "chatId", chatId,
                                "sender", "user123",
                                "text", "Пример сообщения",
                                "ts", "2024-01-15T10:30:00Z"
                        )
                )
        ));
    }

    @Operation(
            summary = "Получить примеры кода",
            description = "Возвращает примеры кода для различных языков программирования"
    )
    @GetMapping("/examples")
    public ResponseEntity<Map<String, Object>> getCodeExamples() {
        return ResponseEntity.ok(Map.of(
                "javascript", Map.of(
                        "description", "JavaScript с использованием STOMP.js",
                        "code", "// Подключение к WebSocket\n" +
                                "const socket = new WebSocket('ws://localhost:8080/ws');\n" +
                                "const stompClient = Stomp.over(socket);\n\n" +
                                "// Аутентификация и подключение\n" +
                                "stompClient.connect(\n" +
                                "    { 'Authorization': 'Bearer YOUR_JWT_TOKEN' },\n" +
                                "    function (frame) {\n" +
                                "        console.log('Подключено к WebSocket');\n\n" +
                                "        // Подписка на сообщения чата\n" +
                                "        stompClient.subscribe('/topic/chat/CHAT_ID', function (message) {\n" +
                                "            const messageData = JSON.parse(message.body);\n" +
                                "            console.log('Новое сообщение:', messageData);\n" +
                                "        });\n\n" +
                                "        // Отправка сообщения\n" +
                                "        stompClient.send('/app/chats/CHAT_ID/send', {}, JSON.stringify({\n" +
                                "            text: 'Привет, мир!'\n" +
                                "        }));\n" +
                                "    },\n" +
                                "    function (error) {\n" +
                                "        console.error('Ошибка подключения:', error);\n" +
                                "    }\n" +
                                ");"
                ),
                "python", Map.of(
                        "description", "Python с использованием websocket-client и stomp.py",
                        "code", "import websocket\n" +
                                "import json\n" +
                                "import stomp\n\n" +
                                "# Создание STOMP соединения\n" +
                                "conn = stomp.Connection()\n" +
                                "conn.connect('localhost', 8080, wait=True)\n\n" +
                                "# Подписка на сообщения\n" +
                                "def message_handler(frame):\n" +
                                "    message_data = json.loads(frame.body)\n" +
                                "    print(f\"Получено сообщение: {message_data}\")\n\n" +
                                "conn.subscribe('/topic/chat/CHAT_ID', message_handler)\n\n" +
                                "# Отправка сообщения\n" +
                                "message = {'text': 'Привет, мир!'}\n" +
                                "conn.send('/app/chats/CHAT_ID/send', json.dumps(message))"
                ),
                "java", Map.of(
                        "description", "Java с использованием Spring WebSocket",
                        "code", "// WebSocket клиент с Spring\n" +
                                "WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(\n" +
                                "    List.of(new WebSocketTransport(new StandardWebSocketClient()))\n" +
                                "));\n\n" +
                                "StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {\n" +
                                "    @Override\n" +
                                "    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {\n" +
                                "        // Подписка на сообщения\n" +
                                "        session.subscribe(\"/topic/chat/CHAT_ID\", new StompFrameHandler() {\n" +
                                "            @Override\n" +
                                "            public Type getPayloadType(StompHeaders headers) {\n" +
                                "                return ChatMessage.class;\n" +
                                "            }\n\n" +
                                "            @Override\n" +
                                "            public void handleFrame(StompHeaders headers, Object payload) {\n" +
                                "                ChatMessage message = (ChatMessage) payload;\n" +
                                "                System.out.println(\"Получено сообщение: \" + message);\n" +
                                "            }\n" +
                                "        });\n\n" +
                                "        // Отправка сообщения\n" +
                                "        SendMessageRequest request = new SendMessageRequest(\"Привет, мир!\");\n" +
                                "        session.send(\"/app/chats/CHAT_ID/send\", request);\n" +
                                "    }\n" +
                                "};\n\n" +
                                "StompHeaders connectHeaders = new StompHeaders();\n" +
                                "connectHeaders.add(\"Authorization\", \"Bearer YOUR_JWT_TOKEN\");\n\n" +
                                "stompClient.connect(\"ws://localhost:8080/ws\", sessionHandler, connectHeaders);"
                )
        ));
    }
}
