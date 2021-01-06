FROM openjdk:8

EXPOSE 8080

COPY target/*.jar sns-itda.jar

ENTRYPOINT ["java -jar ${JAVA_OPTS} /sns-itda.jar"]