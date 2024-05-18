FROM openjdk:21-jdk
VOLUME /tmp
ARG JAR_FILE
COPY build/libs/data-analyser-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
