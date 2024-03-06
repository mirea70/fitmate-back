FROM openjdk:17-jdk
ARG JAR_FILE=/home/opc/fitmate-back/modules/app/app-mate/build/libs/app-mate-0.0.1-SNAPSHOT.jar
ARG ORACLE_KEY_FILE=/home/opc/wallet/Wallet_fitmateMain
COPY ${JAR_FILE} mate.jar
COPY ${ORACLE_KEY_FILE} /home/opc/wallet/Wallet_fitmateMain

ENTRYPOINT ["java", "-jar", "mate.jar"]
