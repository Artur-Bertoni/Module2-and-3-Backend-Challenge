FROM openjdk:11

VOLUME /tmp
ARG JAR_FILE=/*.jar
COPY ./target/${JAR_FILE} producer.jar

ENTRYPOINT ["java", "-jar", "producer.jar"]