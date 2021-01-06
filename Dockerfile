FROM openjdk:8

EXPOSE 8080

COPY target/*.jar sns-itda.jar

ENTRYPOINT ["java", "${JAVA_OPTS}", "-jar", "/sns-itda.jar"]