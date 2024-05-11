FROM gradle:8.7.0-jdk17 AS build

WORKDIR /app

COPY build.gradle .

COPY settings.gradle .

COPY src src

RUN gradle build

FROM openjdk:17-jdk-alpine

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
