# Messenger Application

# Messenger Application

Современный мессенджер с WebSocket API, использующий Spring Boot 3.x и SpringDoc для документации.

## Настройка переменных окружения

Для корректной работы приложения необходимо настроить следующие переменные окружения:

### JWT Configuration
```bash
# JWT секретный ключ (Base64)
JWT_SECRET=bW9ja19yYW5kb21fc3VwZXJfc2VjcmV0X2Jhc2U2NF9ieXRlcw==

# Время жизни JWT токена в миллисекундах (по умолчанию 1 час)
JWT_EXPIRATION_MS=3600000
```

### Crypto Configuration
```bash
# Ключ шифрования для паролей и сообщений (Base64, 32 байта)
# Сгенерируй новый ключ: openssl rand -base64 32
CRYPTO_KEY_BASE64=u0l/h+fbhhCe0KAr3q2wkzuRBIRjYwmoq721+ZbTtFo=
```

### Database Configuration
```bash
DB_URL=jdbc:postgresql://localhost:5432/chat_v2
DB_USERNAME=postgres
DB_PASSWORD=1234
```

### MinIO Configuration (опционально)
```bash
MINIO_ENDPOINT=https://s3.students.alabuga.space
MINIO_REGION=ru-cod-4-2
MINIO_ACCESS_KEY=bOWbfv5DHJgsq8PdzTKT
MINIO_SECRET_KEY=O5HY8pbfdXgKTHZiW5XyfK21cGKGeYh5RPfqD3uM
MINIO_BUCKET=alabuga-hr-chat-dev
```

## Генерация ключей

### JWT Secret
```bash
# Генерация JWT секрета
openssl rand -base64 32
```

### Crypto Key
```bash
# Генерация ключа шифрования
openssl rand -base64 32
```

## Запуск приложения

### Требования
- PostgreSQL сервер должен быть запущен
- База данных `chat_v2` должна быть создана

### Настройка PostgreSQL

#### Вариант 1: Docker (рекомендуется)
```bash
# Запуск PostgreSQL в Docker
docker-compose up -d postgres

# Проверка статуса
docker-compose ps
```

#### Вариант 2: Локальная установка
1. **Установите PostgreSQL** (если еще не установлен)
2. **Создайте базу данных:**
   ```bash
   # Подключитесь к PostgreSQL
   psql -U postgres
   
   # Создайте базу данных
   CREATE DATABASE chat_v2;
   
   # Или используйте готовый скрипт
   psql -U postgres -f setup-database.sql
   ```

### Настройка переменных окружения
```bash
# Для Docker (значения по умолчанию)
export DB_URL=jdbc:postgresql://localhost:5432/chat_v2
export DB_USERNAME=postgres
export DB_PASSWORD=1234

# Для локальной установки (замените на ваши значения)
# export DB_URL=jdbc:postgresql://localhost:5433/chat_v2
# export DB_USERNAME=postgres
# export DB_PASSWORD=your_password
```

### Запуск
```bash
mvn spring-boot:run
```

## API Documentation

Swagger UI доступен по адресу: http://localhost:8080/swagger-ui.html

## WebSocket API

Приложение поддерживает WebSocket соединения через STOMP протокол:
- WebSocket endpoint: `ws://localhost:8080/ws`
- Топики: `/topic/chat/{chatId}`
- Приложение endpoints: `/app/chats/{chatId}/send`

## Технологии

- Spring Boot 3.3.2
- Spring WebSocket
- Spring Security с JWT
- Spring Data JPA
- PostgreSQL
- SpringDoc OpenAPI
- Lombok
