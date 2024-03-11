FROM maven:3.8.4-openjdk-17 as build

WORKDIR /app

COPY pom.xml /app/pom.xml
COPY ./calendar/pom.xml /app/calendar/pom.xml
COPY ./calendar/src /app/calendar/src

RUN mvn -f /app/calendar/pom.xml clean install

RUN ls -la /app/calendar/target

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/calendar/target/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]