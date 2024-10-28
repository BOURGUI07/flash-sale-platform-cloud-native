# Use the OpenJDK 22 image as a base
FROM openjdk:22-jdk-oracle

# Set the working directory
WORKDIR /app

# Copy the Spring Boot JAR file to the container
COPY target/config-service-0.0.1-SNAPSHOT.jar config-service-0.0.1-SNAPSHOT.jar


# Specify the command to run the application with the Java agent
ENTRYPOINT ["java", "-jar", "config-service-0.0.1-SNAPSHOT.jar"]
