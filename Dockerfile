FROM openjdk:11

VOLUME /tmp
ARG JAR_FILE
ADD ./target/${JAR_FILE} desafioBackendModulo2.jar

ENTRYPOINT ["java", "-jar", "desafioBackendModulo2.jar"]