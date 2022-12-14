FROM openjdk:11

VOLUME /tmp
ARG JAR_FILE=/*.jar
COPY ./target/${JAR_FILE} desafio2.jar

ENTRYPOINT ["java", "-jar", "desafio2.jar"]