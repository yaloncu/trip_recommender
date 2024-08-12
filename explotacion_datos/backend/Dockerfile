FROM openjdk:17-jdk-slim
WORKDIR /app

COPY backend/target/mybackend-0.0.1-SNAPSHOT.jar /app/explotacion_datos.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/explotacion_datos.jar"]
