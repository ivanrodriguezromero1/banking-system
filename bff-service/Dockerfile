# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY target/bff-service-1.0.0-SNAPSHOT.jar bff-service.jar

# Make port 8083 available to the world outside this container
EXPOSE 8083

# Run the application
ENTRYPOINT ["java", "-jar", "bff-service.jar"]
