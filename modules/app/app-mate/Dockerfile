FROM openjdk:17-jdk
ARG JAR_FILE=modules/app/app-mate/build/libs/*.jar
#ARG ORACLE_KEY_FILE=/home/opc/wallet/Wallet_fitmateMain/
COPY ${JAR_FILE} mate.jar
COPY ${ORACLE_KEY_FILE} /home/opc/wallet/Wallet_fitmateMain/

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prd" , "mate.jar"]
