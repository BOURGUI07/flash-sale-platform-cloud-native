# Use the OpenJDK 22 image as a base
FROM openjdk:22-jdk-oracle

# Set the working directory
WORKDIR /app

# Copy the Spring Boot JAR file to the container
COPY target/config-service-0.0.1-SNAPSHOT.jar config-service-0.0.1-SNAPSHOT.jar

# Copy the OpenTelemetry Java agent from the libs folder to the container
COPY libs/opentelemetry-javaagent.jar opentelemetry-javaagent.jar

# Specify the command to run the application with the Java agent
ENTRYPOINT ["java", "-javaagent:opentelemetry-javaagent.jar", "-jar", "config-service-0.0.1-SNAPSHOT.jar"]
