# Etapa de construcción
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Crear directorio de trabajo
WORKDIR /app

# Copiar el archivo pom.xml y descargar dependencias (cache eficiente)
COPY backend/pom.xml ./
RUN mvn dependency:go-offline

# Copiar el resto del proyecto
COPY backend/ ./

# Compilar el proyecto y generar el JAR
RUN mvn clean install -DskipTests

# Etapa de ejecución
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiar el JAR desde la etapa de build
COPY --from=build /app/target/*.jar /app/explotacion_datos.jar

# Copiar archivo de claves Firebase si lo necesitas en producción
COPY backend/src/main/resources/firebase-service-account.json /app/firebase/firebase-service-account.json

# Exponer el puerto 8080
EXPOSE 8080

# Ejecutar el JAR
ENTRYPOINT ["java", "-jar", "/app/explotacion_datos.jar"]
