FROM openjdk:8

VOLUME /tmp

EXPOSE 8080

COPY target/sns-server-0.0.1-SNAPSHOT.jar sns-itda.jar

ENV SPRING_CONFIG_LOCATION /home/ubuntu/sns-itda/deploy/application.properties

ENTRYPOINT ["java", "-jar", "/sns-itda.jar",
            "--spring.config.location=${SPRING_CONFIG_LOCATION}"]