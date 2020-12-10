FROM openjdk:8

EXPOSE 8080

COPY target/*.jar sns-itda.jar

ENV JAVA_OPTS $JAVA_OPTS

ENTRYPOINT ["java", "-jar", "/sns-itda.jar", "${JAVA_OPTS}"]