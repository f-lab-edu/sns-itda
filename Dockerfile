FROM openjdk:8

VOLUME /tmp

EXPOSE 8080

ADD target/sns-server-0.0.1-SNAPSHOT.jar sns-server-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/sns-server-0.0.1-SNAPSHOT.jar",
"--spring.config.location=$/home/ubuntu/sns-itda/deploy/application.properties"]