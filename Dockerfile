FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./target/clip_management-1.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "clip_management-1.0-SNAPSHOT.jar"]