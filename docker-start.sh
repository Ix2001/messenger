#!/bin/bash

# Скрипт для запуска Messenger приложения в Docker

set -e

echo "🚀 Запуск Messenger приложения в Docker..."

# Проверяем, установлен ли Docker
if ! command -v docker &> /dev/null; then
    echo "❌ Docker не установлен. Пожалуйста, установите Docker."
    exit 1
fi

# Проверяем, установлен ли Docker Compose
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose не установлен. Пожалуйста, установите Docker Compose."
    exit 1
fi

# Останавливаем существующие контейнеры
echo "🛑 Остановка существующих контейнеров..."
docker-compose down

# Собираем и запускаем приложение
echo "🔨 Сборка и запуск приложения..."
docker-compose up --build -d

# Ждем, пока приложение запустится
echo "⏳ Ожидание запуска приложения..."
sleep 30

# Проверяем статус
echo "📊 Статус контейнеров:"
docker-compose ps

# Проверяем логи приложения
echo "📋 Логи приложения:"
docker-compose logs messenger-app --tail=20

echo "✅ Приложение запущено!"
echo "🌐 Swagger UI: http://localhost:8080/swagger-ui.html"
echo "🗄️  PostgreSQL: localhost:5433"
echo "📊 pgAdmin (dev): http://localhost:5050 (admin@messenger.com / admin123)"
