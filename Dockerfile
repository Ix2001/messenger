# Используем официальный образ OpenJDK 21
FROM openjdk:21-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем Maven wrapper и pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Делаем mvnw исполняемым
RUN chmod +x mvnw

# Скачиваем зависимости (этот слой будет кэшироваться)
RUN ./mvnw dependency:go-offline -B

# Копируем исходный код
COPY src src

# Собираем приложение
RUN ./mvnw clean package -DskipTests

# Создаем пользователя для безопасности
RUN addgroup --system appuser && adduser --system --ingroup appuser appuser

# Переименовываем JAR файл для удобства
RUN mv target/*.jar app.jar

# Меняем владельца файлов
RUN chown -R appuser:appuser /app

# Переключаемся на непривилегированного пользователя
USER appuser

# Открываем порт
EXPOSE 8080

# Устанавливаем переменные окружения по умолчанию
ENV SPRING_PROFILES_ACTIVE=postgres
ENV DB_URL=jdbc:postgresql://postgres:5432/chat_v2
ENV DB_USERNAME=postgres
ENV DB_PASSWORD=1234
ENV JWT_SECRET=1YIbsNOQdqj6bNpDPrVz7Up8vGLkEmCGQGPf0HsGG7w=
ENV JWT_EXPIRATION_MS=3600000
ENV CRYPTO_KEY_BASE64=1YIbsNOQdqj6bNpDPrVz7Up8vGLkEmCGQGPf0HsGG7w=

# Команда запуска
ENTRYPOINT ["java", "-jar", "app.jar"]
