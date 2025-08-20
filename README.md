# 🔌 Messenger WebSocket API

Современный мессенджер с WebSocket API, использующий Spring Boot 3.x и SpringDoc для документации.

## 🚀 Быстрый старт

### 1. Запуск приложения

```bash
# Сборка проекта
./mvnw clean compile

# Запуск с H2 базой данных (по умолчанию)
./mvnw spring-boot:run

# Запуск с PostgreSQL базой данных
./mvnw spring-boot:run -Dspring.profiles.active=postgres
```

### 2. Доступные URL

После запуска приложения доступны следующие URL:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **WebSocket API Test Page**: `http://localhost:8080/websocket-api.html`
- **WebSocket API Info**: `http://localhost:8080/api/websocket-docs/info`
- **Примеры кода**: `http://localhost:8080/api/websocket-docs/examples`
- **H2 Console** (только для H2 профиля): `http://localhost:8080/h2-console`

## 📋 Основные возможности

### WebSocket API
- **STOMP протокол** для WebSocket сообщений
- **JWT аутентификация** для всех соединений
- **Реальное время** обмен сообщениями
- **Подписка на чаты** для получения сообщений

### Документация
- **SpringDoc OpenAPI** для автоматической генерации документации
- **Swagger UI** для интерактивного тестирования
- **HTML тестовая страница** с STOMP клиентом
- **REST API** для получения информации о WebSocket endpoints

### Тестирование
- **Node.js скрипт** для автоматизированного тестирования
- **Примеры кода** для JavaScript, Python, Java
- **Интерактивная HTML страница** для ручного тестирования

## 🔧 Технологии

- **Spring Boot 3.3.2** - основной фреймворк
- **Spring WebSocket** - поддержка WebSocket
- **STOMP** - протокол для WebSocket сообщений
- **Spring Security** - аутентификация и авторизация
- **JWT** - токены для аутентификации
- **SpringDoc OpenAPI 2.6.0** - документация API
- **H2** - встроенная база данных для разработки (по умолчанию)
- **PostgreSQL** - база данных для продакшена

## 📖 Документация

- [WebSocket API Documentation](WEBSOCKET_API_README.md) - полная документация WebSocket API
- [Quick Start Guide](QUICK_START.md) - инструкции по быстрому старту
- [Changelog](CHANGELOG.md) - история изменений

## 🗄️ Профили базы данных

### H2 (по умолчанию)
- Встроенная база данных в памяти
- Автоматическое создание схемы при запуске
- H2 Console доступен по адресу `/h2-console`
- Идеально для разработки и тестирования

### PostgreSQL
- Для продакшена
- Требует запущенный PostgreSQL сервер
- Используйте профиль: `-Dspring.profiles.active=postgres`

## 🧪 Тестирование

### HTML страница
1. Откройте `http://localhost:8080/websocket-api.html`
2. Введите JWT токен
3. Нажмите "Подключиться"
4. Протестируйте отправку и получение сообщений

### Node.js скрипт
```bash
# Установка зависимостей
npm install

# Запуск теста
node test-websocket.js "YOUR_JWT_TOKEN" "CHAT_ID"
```

### Swagger UI
1. Откройте `http://localhost:8080/swagger-ui.html`
2. Найдите раздел "WebSocket Documentation"
3. Протестируйте endpoints

## 🔒 Безопасность

- JWT аутентификация для всех WebSocket соединений
- Проверка токенов на каждом WebSocket сообщении
- Безопасная передача токенов через STOMP заголовки

## 📊 Производительность

- Оптимизированная конфигурация SpringDoc
- Минимальные накладные расходы на документацию
- Эффективная обработка WebSocket сообщений

## 🎯 Результат

Теперь у вас есть полноценная документация WebSocket API с использованием SpringDoc, которая включает:

- ✅ Интерактивную документацию через Swagger UI
- ✅ REST API для получения информации о WebSocket endpoints
- ✅ HTML страницу для тестирования
- ✅ Node.js скрипт для автоматизированного тестирования
- ✅ Примеры кода для разных языков программирования
- ✅ Подробную документацию и инструкции
- ✅ Совместимость с Spring Boot 3.x

SpringDoc успешно интегрирован и предоставляет аналог Swagger для WebSocket API! 🎉
