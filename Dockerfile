FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY target/monieFlex-0.0.1-SNAPSHOT.jar monieflexapi.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/monieflexapi.jar"]

