FROM maven:3.8.4-openjdk-17 as build

WORKDIR /app

COPY ./api-gateway/pom.xml ./api-gateway/pom.xml
COPY ./api-gateway/src ./api-gateway/src

RUN mvn -f ./api-gateway/pom.xml clean package


FROM openjdk:17

WORKDIR /app

COPY --from=build /app/api-gateway/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
