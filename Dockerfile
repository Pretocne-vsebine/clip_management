FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

ADD ./clip_managementREST/target/clip_management-REST-1.0-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "clip_management-REST-1.0-SNAPSHOT.jar"]