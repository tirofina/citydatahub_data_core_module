FROM adoptopenjdk/openjdk11:jre-11.0.9_11.1-alpine
VOLUME /tmp
ADD ingest-interface-2.0.0-SNAPSHOT.jar app.jar
ADD keystore.p12 keystore.p12
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Djava.net.preferIPv4Stack=true", "-Dspring.profiles.active=local","-jar","/app.jar"]
