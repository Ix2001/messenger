# WebSocket API Documentation

## Обзор

Этот проект использует **SpringDoc OpenAPI** для документации WebSocket API, что является современным аналогом Swagger для WebSocket endpoints. SpringDoc предоставляет удобный интерфейс для тестирования и документирования WebSocket API, совместимый с Spring Boot 3.x.

## Архитектура

### Компоненты
- **SpringDoc OpenAPI 2.6.0** - основная библиотека для документации API
- **STOMP** - протокол для WebSocket сообщений
- **JWT** - аутентификация

### Конфигурация

#### 1. Зависимости (pom.xml)
```xml
<!-- SpringDoc OpenAPI for WebSocket documentation -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.6.0</version>
</dependency>
```

#### 2. Конфигурация SpringDoc
```java
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Messenger WebSocket API")
                        .description("WebSocket API для мессенджера с поддержкой STOMP протокола")
                        .version("1.1.0"));
    }
}
```

#### 3. Настройки application.yml
```yaml
springdoc:
  api-docs:
    enabled: true
  swagger-ui:
    enabled: true
    path: /swagger-ui.html
```

## WebSocket Endpoints

### 1. Подключение к WebSocket
```
URL: ws://localhost:8080/ws
Protocol: STOMP over WebSocket
Authentication: JWT Bearer Token
```

### 2. Отправка сообщений
```
Endpoint: /app/chats/{chatId}/send
Method: SEND
Payload: SendMessageRequest
```

**Пример запроса:**
```json
{
  "text": "Привет, мир!"
}
```

### 3. Подписка на сообщения
```
Topic: /topic/chat/{chatId}
Method: SUBSCRIBE
```

**Пример ответа:**
```json
{
  "chatId": "123e4567-e89b-12d3-a456-426614174000",
  "sender": "user123",
  "text": "Привет, мир!",
  "ts": "2024-01-15T10:30:00Z"
}
```

## Доступ к документации

### 1. Swagger UI (SpringDoc)
```
URL: http://localhost:8080/swagger-ui.html
```

### 2. WebSocket API Test Page
```
URL: http://localhost:8080/websocket-api.html
```

### 3. WebSocket Documentation API
```
URL: http://localhost:8080/api/websocket-docs/info
```

### 4. Примеры кода
```
URL: http://localhost:8080/api/websocket-docs/examples
```

## Использование

### 1. Запуск приложения
```bash
mvn spring-boot:run
```

### 2. Получение JWT токена
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"password"}'
```

### 3. Тестирование WebSocket API

#### Через HTML страницу:
1. Откройте `http://localhost:8080/websocket-api.html`
2. Введите JWT токен
3. Нажмите "Подключиться"
4. Введите Chat ID и сообщение
5. Нажмите "Отправить сообщение"

#### Через STOMP клиент:
```javascript
const socket = new WebSocket('ws://localhost:8080/ws');
const stompClient = Stomp.over(socket);

stompClient.connect(
  { 'Authorization': 'Bearer YOUR_JWT_TOKEN' },
  function (frame) {
    // Подписка на сообщения
    stompClient.subscribe('/topic/chat/CHAT_ID', function (message) {
      console.log('Получено:', JSON.parse(message.body));
    });
    
    // Отправка сообщения
    stompClient.send('/app/chats/CHAT_ID/send', {}, JSON.stringify({
      text: 'Привет!'
    }));
  }
);
```

#### Через Node.js тестовый скрипт:
```bash
# Установка зависимостей
npm install

# Запуск теста
node test-websocket.js "YOUR_JWT_TOKEN" "CHAT_ID"
```

## Структура проекта

```
src/main/java/com/example/messenger/
├── config/
│   ├── SpringfoxConfig.java          # Конфигурация Springfox
│   ├── OpenApiConfig.java            # Конфигурация OpenAPI
│   └── WebSocketConfig.java          # Конфигурация WebSocket
├── controller/
│   ├── WebSocketApiController.java   # REST контроллер для WebSocket документации
│   └── ChatController.java           # REST контроллер чатов
├── ws/
│   └── ChatWsController.java         # WebSocket контроллер
└── ...

src/main/resources/
├── static/
│   └── websocket-api.html            # HTML страница для тестирования
└── application.yml                   # Конфигурация приложения
```

## Аннотации Springfox

### Основные аннотации:
- `@Api` - описание контроллера
- `@ApiOperation` - описание операции
- `@ApiParam` - описание параметра
- `@ApiModel` - описание модели
- `@ApiModelProperty` - описание свойства модели

### Пример использования:
```java
@Api(tags = "WebSocket API", description = "WebSocket endpoints для мессенджера")
@RestController
public class WebSocketApiController {

    @ApiOperation(
        value = "Отправить сообщение в чат",
        notes = "WebSocket endpoint: /app/chats/{chatId}/send"
    )
    @PostMapping("/chats/{chatId}/send")
    public ResponseEntity<Map<String, Object>> sendMessageExample(
        @ApiParam(value = "ID чата", required = true)
        @PathVariable String chatId,
        @ApiParam(value = "Данные сообщения", required = true)
        @RequestBody SendMessageRequest request
    ) {
        // ...
    }
}
```

## Безопасность

### JWT Аутентификация
- Все WebSocket соединения требуют JWT токен
- Токен передается в заголовке STOMP: `Authorization: Bearer <token>`
- Токен проверяется на каждом WebSocket сообщении

### CORS настройки
```java
@Configuration
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .withSockJS();
    }
}
```

## Отладка

### Логирование
```yaml
logging:
  level:
    org.springframework.web.socket: DEBUG
    org.springframework.messaging: DEBUG
    org.springframework.web.socket.sockjs: DEBUG
```

### Мониторинг соединений
- Все WebSocket соединения логируются
- Ошибки аутентификации записываются в лог
- Статистика сообщений доступна через JMX

## Производительность

### Рекомендации:
1. Используйте пул соединений для WebSocket клиентов
2. Ограничьте количество одновременных подписок
3. Реализуйте heartbeat для проверки соединений
4. Используйте кэширование для часто запрашиваемых данных

### Мониторинг:
- Количество активных WebSocket соединений
- Скорость обработки сообщений
- Задержка доставки сообщений
- Использование памяти

## Заключение

Springfox предоставляет мощные инструменты для документации WebSocket API, делая его доступным и понятным для разработчиков. Интеграция с существующим Springwolf обеспечивает полное покрытие как REST, так и WebSocket endpoints.
