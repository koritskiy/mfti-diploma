FROM openjdk:8-jdk-alpine
WORKDIR /tmp
COPY *.jar /gateway.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=codemika", "-jar", "/gateway.jar"]