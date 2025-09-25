FROM openjdk:17-jdk-slim

WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN ./mvnw dependency:go-offline
COPY src/ src/

RUN ./mvnw package -DskipTests
EXPOSE 8080
CMD ["java", "-jar", "target/Kiddit-0.0.1-SNAPSHOT.jar"]