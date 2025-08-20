# 🚀 Быстрый старт WebSocket API

## Шаг 1: Запуск приложения

```bash
# Сборка проекта
./mvnw clean compile

# Запуск с H2 базой данных (по умолчанию)
./mvnw spring-boot:run

# Или запуск с PostgreSQL (если у вас запущен PostgreSQL)
./mvnw spring-boot:run -Dspring.profiles.active=postgres
```

## Шаг 2: Получение JWT токена

```bash
# Регистрация пользователя
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com"
  }'

# Вход в систему
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

## Шаг 3: Создание чата

```bash
# Создание чата (используйте полученный JWT токен)
curl -X POST http://localhost:8080/api/chats \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Тестовый чат",
    "creatorId": "USER_ID",
    "memberIds": ["USER_ID"]
  }'
```

## Шаг 4: Тестирование WebSocket API

### Вариант A: HTML страница
1. Откройте `http://localhost:8080/websocket-api.html`
2. Введите JWT токен
3. Нажмите "Подключиться"
4. Введите Chat ID и сообщение
5. Нажмите "Отправить сообщение"

### Вариант B: Node.js скрипт
```bash
# Установка зависимостей
npm install

# Запуск теста
node test-websocket.js "YOUR_JWT_TOKEN" "CHAT_ID"
```

### Вариант C: Swagger UI
1. Откройте `http://localhost:8080/swagger-ui.html`
2. Найдите раздел "WebSocket Documentation"
3. Протестируйте endpoints

## Шаг 5: Проверка документации

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **WebSocket API Info**: `http://localhost:8080/api/websocket-docs/info`
- **Примеры кода**: `http://localhost:8080/api/websocket-docs/examples`
- **H2 Console** (только для H2 профиля): `http://localhost:8080/h2-console`
- **HTML тестовая страница**: `http://localhost:8080/websocket-api.html`

## 🔧 Устранение неполадок

### Ошибка подключения к WebSocket
- Убедитесь, что приложение запущено на порту 8080
- Проверьте, что JWT токен действителен
- Проверьте логи приложения

### Ошибка аутентификации
- Убедитесь, что JWT токен передается в заголовке `Authorization: Bearer <token>`
- Проверьте, что токен не истек

### Ошибка отправки сообщения
- Убедитесь, что Chat ID существует
- Проверьте, что пользователь является участником чата

## 📋 Полезные команды

```bash
# Проверка статуса приложения
curl http://localhost:8080/actuator/health

# Получение информации о WebSocket API
curl http://localhost:8080/api/websocket-docs/info

# Получение примеров кода
curl http://localhost:8080/api/websocket-docs/examples
```

## 🎯 Готово!

Теперь у вас есть полностью работающий WebSocket API с документацией SpringDoc! 🎉
