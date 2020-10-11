FROM openjdk:8-jdk-alpine
WORKDIR ${folder}
COPY target/load-balancer-runner-jar-with-dependencies.jar ${folder}
EXPOSE 9091
CMD java -XX:+PrintFlagsFinal ${JAVA_OPTS} -jar load-balancer-runner-jar-with-dependencies.jar
