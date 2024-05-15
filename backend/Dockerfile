FROM maven:3.8.3-openjdk-17 AS build

COPY src /app/src
COPY pom.xml /app

RUN mvn -f /app/pom.xml clean package -DskipTests

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/target/kw3.jar"]
