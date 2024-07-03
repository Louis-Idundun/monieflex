FROM amazoncorretto:17
VOLUME /tmp
EXPOSE 8080
COPY target/monieFlex-0.0.1-SNAPSHOT.jar monieflexapi.jar
ENTRYPOINT ["java","-jar","/monieflexapi.jar"]

