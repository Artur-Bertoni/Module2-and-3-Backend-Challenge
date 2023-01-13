FROM openjdk:11

VOLUME /tmp
ARG JAR_FILE=/*.jar
COPY ./target/${JAR_FILE} produto-api.jar

ENTRYPOINT ["java", "-jar", "produto-api.jar"]