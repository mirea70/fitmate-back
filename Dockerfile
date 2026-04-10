FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew && ./gradlew clean test :adapter:bootJar
ARG JAR_FILE=modules/adapter/build/libs/*.jar
COPY ${JAR_FILE} fitmate.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prd", "-Dspring.config.location=./resources/ymls/", "fitmate.jar"]