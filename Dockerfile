# Fase 1: Construcción (Build) usando Maven y Java 17
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copiar el archivo de configuración de dependencias
COPY pom.xml .

# Descargar las dependencias (capa de caché para acelerar futuros builds)
RUN mvn dependency:go-offline -B

# Copiar el código fuente del proyecto
COPY src ./src

# Compilar y empaquetar el JAR omitiendo las pruebas unitarias para el empaquetado final
RUN mvn clean package -DskipTests

# Fase 2: Ejecución (Run) usando una imagen ligera de Java
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el archivo .jar generado en la fase anterior
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto en el que corre tu API
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]