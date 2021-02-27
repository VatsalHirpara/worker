FROM openjdk:11.0-jre

WORKDIR /tmp

COPY target/worker-0.0.1-SNAPSHOT.jar /tmp/worker-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/tmp/worker-0.0.1-SNAPSHOT.jar"]

EXPOSE 9099