FROM openjdk:17-jdk
ARG JAR_FILE=modules/adapter/build/libs/*.jar
COPY ${JAR_FILE} fitmate.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prd", "-Dspring.config.location=./resources/ymls/", "fitmate.jar"]