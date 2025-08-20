-- Создание базы данных для Messenger приложения
-- Выполните этот скрипт от имени пользователя postgres
-- Примечание: При использовании Docker порт 5433, при локальной установке - 5432

-- Создание базы данных
CREATE DATABASE chat_v2;

-- Подключение к созданной базе данных
\c chat_v2;

-- Создание пользователя (опционально, если хотите использовать отдельного пользователя)
-- CREATE USER messenger_user WITH PASSWORD 'your_password';
-- GRANT ALL PRIVILEGES ON DATABASE chat_v2 TO messenger_user;

-- Примечание: Таблицы будут созданы автоматически Hibernate при первом запуске приложения
-- благодаря настройке hibernate.ddl-auto: update
