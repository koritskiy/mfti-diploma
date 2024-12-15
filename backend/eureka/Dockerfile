FROM openjdk:8-jdk-alpine
WORKDIR /tmp
COPY target/*.jar /eureka.jar

EXPOSE 8761

ENTRYPOINT ["java", "-Dspring.profiles.active=codemika", "-jar", "/eureka.jar"]