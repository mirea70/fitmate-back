FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} mate.jar
COPY ${ORACLE_KEY_FILE} /home/ubuntu/wallet/Wallet_fitmateMain/

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prd", "-Dspring.config.location=file:/home/ubuntu/resources" , "mate.jar"]
