FROM maven:3.9.8-eclipse-temurin-21-alpine AS build

COPY pom.xml ./
COPY .mvn .mvn

COPY src src

RUN mvn clean install -DskipTests


FROM maven:3.9.8-eclipse-temurin-21-alpine

WORKDIR /app

COPY --from=build target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]