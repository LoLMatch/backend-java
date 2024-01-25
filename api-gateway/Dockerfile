FROM maven:3.8.4-openjdk-17 as build

WORKDIR /app

COPY ./api-gateway/pom.xml /app/api-gateway/pom.xml
COPY ./api-gateway/src /app/api-gateway/src

COPY ./pom.xml /app/pom.xml

RUN mvn -f /app/api-gateway/pom.xml clean install

FROM openjdk:17

WORKDIR /app

COPY --from=build /app/api-gateway/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
