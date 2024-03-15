FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} mate.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prd", "-Dspring.config.location=./resources/ymls/", "mate.jar"]