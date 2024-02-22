FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests
FROM openjdk:17.0.1-jdk-slim
COPY --form=build /target/pizza-parlor-0.0.1-SNAPSHOT.jar pizza-parlor.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","pizza-parlor.jar"]