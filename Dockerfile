FROM openjdk:17-jdk

COPY ./build/libs/todo_backend-0.0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]