# Mavenでビルド
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# 実行用イメージ
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/message-board-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]