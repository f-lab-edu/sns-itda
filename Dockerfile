FROM openjdk:8

EXPOSE 8080

COPY target/sns-server-0.0.1-SNAPSHOT.jar sns-itda.jar

ENV SPRING_PROFILES_ACTIVE $SPRING_PROFILES_ACTIVE

ENTRYPOINT ["java", "-jar", "/sns-itda.jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}"]