FROM openjdk:21-jdk-slim


WORKDIR /app

COPY target/account-0.0.1-SNAPSHOT.jar /app/accountapplication.jar

ENTRYPOINT ["java", "-jar", "accountapplication.jar"]