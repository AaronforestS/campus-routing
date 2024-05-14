FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/campus_routing_backend-1.0.jar campus_routing_backend.jar
COPY --from=build database.sqlite database.sqlite
EXPOSE 8080
ENTRYPOINT ["java","-jar","campus_routing_backend.jar"]