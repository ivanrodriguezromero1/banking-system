# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY target/product-service-1.0.0-SNAPSHOT.jar product-service.jar

# Make port 8082 available to the world outside this container
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "product-service.jar"]
