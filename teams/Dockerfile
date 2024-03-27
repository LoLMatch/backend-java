FROM maven:3.8.4-openjdk-17 as build

WORKDIR /app

COPY ./pom.xml /app/pom.xml
COPY ./teams/pom.xml /app/teams/pom.xml
COPY ./teams/src /app/teams/src

RUN mvn -f /app/teams/pom.xml clean install

RUN ls -la /app/teams/target

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/teams/target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]