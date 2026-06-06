# Этап 1: Сборка (Используем образ с Maven и Java 17)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Собираем JAR файл, пропуская тесты для скорости
RUN mvn clean package -DskipTests

# Этап 2: Запуск (Берем только чистую JRE для минимального веса)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Копируем собранный JAR из первого этапа
COPY --from=build /app/target/*.jar app.jar
# Открываем порт 8080
EXPOSE 8080
# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]