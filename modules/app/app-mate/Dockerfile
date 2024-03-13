FROM openjdk:17-jdk

ARG JAR_FILE=build/libs/*.jar
#ARG RESOURCE_PATH
#ARG ORACLE_KEY_FILE

COPY ${JAR_FILE} mate.jar
#COPY ${RESOURCE_PATH} resources/
#COPY ${RESOURCE_PATH}/application-rdbms.yml application-rdbms.yml
#COPY ${RESOURCE_PATH}/application-redis.yml application-redis.yml
#COPY ${RESOURCE_PATH}/application-security.yml application-security.yml
#COPY ${ORACLE_KEY_FILE} /home/ubuntu/wallet/Wallet_fitmateMain/

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prd", "-Dspring.config.location=./resources/ymls/", "mate.jar"]
