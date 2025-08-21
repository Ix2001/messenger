#!/bin/bash

# Скрипт для остановки Messenger приложения в Docker

echo "🛑 Остановка Messenger приложения..."

# Останавливаем контейнеры
docker-compose down

echo "✅ Приложение остановлено!"

# Показываем статус
echo "📊 Статус контейнеров:"
docker-compose ps
