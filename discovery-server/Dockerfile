FROM maven:3.8.4-openjdk-17 as build

WORKDIR /app

COPY pom.xml /app/pom.xml
COPY ./discovery-server/pom.xml /app/discovery-server/pom.xml
COPY ./discovery-server/src /app/discovery-server/src

RUN mvn -f /app/discovery-server/pom.xml clean install

RUN ls -la /app/discovery-server/target

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/discovery-server/target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]