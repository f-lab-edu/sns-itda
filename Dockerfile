FROM openjdk:8

EXPOSE 8080

COPY target/*.jar sns-itda.jar

ENV JAVA_OPTS $JAVA_OPTS

ENTRYPOINT ["java $JAVA_OPTS", "-jar", "/sns-itda.jar"]