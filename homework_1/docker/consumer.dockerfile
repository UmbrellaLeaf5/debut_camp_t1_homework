FROM anapsix/alpine-java:9

COPY target/homework_1-1.0.jar /app/app.jar

CMD ["java","-jar","/app/app.jar"]
