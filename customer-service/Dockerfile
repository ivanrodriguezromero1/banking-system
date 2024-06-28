# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY target/customer-service-1.0.0-SNAPSHOT.jar customer-service.jar

# Make port 8081 available to the world outside this container
EXPOSE 8081

# Run the application
ENTRYPOINT ["java", "-jar", "customer-service.jar"]
