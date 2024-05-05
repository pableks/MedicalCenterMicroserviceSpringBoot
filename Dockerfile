FROM openjdk:21-ea-24-oracle

WORKDIR /app
COPY target/centromedico-0.0.1-SNAPSHOT.jar app.jar
COPY Wallet_TQTD6LJYL37ELLY3 /app/oracle_wallet/
EXPOSE 8081

CMD [ "java", "-jar", "app.jar" ]