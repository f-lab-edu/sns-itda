FROM openjdk:8

EXPOSE 8080

COPY target/*.jar sns-itda.jar

ENTRYPOINT ["java", "-jar", "/sns-itda.jar", "${_JAVA_OPTIONS}"]