FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8081
ADD /target/file-management-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]