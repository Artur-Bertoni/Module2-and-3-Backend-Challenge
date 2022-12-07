FROM openjdk:11

VOLUME /tmp
ARG JAR_FILE
ADD ./target/${JAR_FILE} desafio2-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "desafio2-0.0.1-SNAPSHOT.jar"]