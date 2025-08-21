.PHONY: help build run stop dev clean logs

# Переменные
APP_NAME = messenger
DOCKER_COMPOSE = docker-compose
DOCKER_COMPOSE_DEV = docker-compose -f docker-compose.dev.yml

help: ## Показать справку
	@echo "Доступные команды:"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

build: ## Собрать Docker образ
	$(DOCKER_COMPOSE) build

run: ## Запустить приложение в Docker
	$(DOCKER_COMPOSE) up -d

stop: ## Остановить приложение
	$(DOCKER_COMPOSE) down

dev: ## Запустить среду разработки
	$(DOCKER_COMPOSE_DEV) up -d

dev-stop: ## Остановить среду разработки
	$(DOCKER_COMPOSE_DEV) down

logs: ## Показать логи приложения
	$(DOCKER_COMPOSE) logs -f messenger-app

logs-db: ## Показать логи базы данных
	$(DOCKER_COMPOSE) logs -f postgres

status: ## Показать статус контейнеров
	$(DOCKER_COMPOSE) ps

clean: ## Очистить все контейнеры и образы
	$(DOCKER_COMPOSE) down -v --rmi all
	docker system prune -f

restart: stop run ## Перезапустить приложение

maven-build: ## Собрать проект с Maven
	mvn clean compile

maven-run: ## Запустить проект с Maven
	mvn spring-boot:run

maven-test: ## Запустить тесты
	mvn test

maven-clean: ## Очистить Maven проект
	mvn clean

docker-build: ## Собрать только Docker образ
	docker build -t $(APP_NAME) .

docker-run: ## Запустить только Docker контейнер
	docker run -p 8080:8080 --name $(APP_NAME) $(APP_NAME)

docker-stop: ## Остановить Docker контейнер
	docker stop $(APP_NAME) && docker rm $(APP_NAME)
