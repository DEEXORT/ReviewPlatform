# Этап 1: Сборка приложения
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Копируем pom.xml и скачиваем зависимости (кэшируем этот слой)
COPY pom.xml .
RUN mvn dependency:go-offline

# Копируем исходники и собираем проект
COPY src ./src
RUN mvn clean package -DskipTests

# Этап 2: Финальный легковесный образ
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Создаем непривилегированного пользователя для безопасности
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Копируем собранный JAR из первого этапа
COPY --from=build /app/target/*.jar app.jar

# Настройки порта
EXPOSE 8080

# Запуск
ENTRYPOINT ["java", "-jar", "app.jar"]