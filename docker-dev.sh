#!/bin/bash

# Скрипт для запуска среды разработки

set -e

echo "🔧 Запуск среды разработки..."

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
docker-compose -f docker-compose.dev.yml down

# Запускаем только инфраструктуру для разработки
echo "🚀 Запуск инфраструктуры для разработки..."
docker-compose -f docker-compose.dev.yml up -d

# Ждем, пока сервисы запустятся
echo "⏳ Ожидание запуска сервисов..."
sleep 15

# Проверяем статус
echo "📊 Статус контейнеров:"
docker-compose -f docker-compose.dev.yml ps

echo "✅ Среда разработки запущена!"
echo "🗄️  PostgreSQL: localhost:5433"
echo "📊 pgAdmin: http://localhost:5050 (admin@messenger.com / admin123)"
echo "🔴 Redis: localhost:6379"
echo ""
echo "💡 Теперь вы можете запустить приложение локально:"
echo "   mvn spring-boot:run"
