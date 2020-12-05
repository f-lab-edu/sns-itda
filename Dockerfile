FROM openjdk:8

VOLUME /tmp

EXPOSE 8080

COPY target/sns-server-0.0.1-SNAPSHOT.jar sns-itda.jar

ENTRYPOINT ["java", "-jar", "/sns-itda.jar"]