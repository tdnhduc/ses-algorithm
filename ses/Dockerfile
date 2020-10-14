FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY target/runner-jar-with-dependencies.jar /a
EXPOSE 8080
CMD java -jar runner-jar-with-dependencies.jar
