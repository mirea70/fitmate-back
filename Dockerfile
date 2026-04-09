FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew && ./gradlew clean test :adapter:bootJar

FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/modules/adapter/build/libs/*.jar fitmate.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prd", "-Dspring.config.location=./resources/ymls/", "fitmate.jar"]
