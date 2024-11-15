# Use a base image with OpenJDK 19
FROM openjdk:19.0.1-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven

# Copy the project files into the container
COPY . /app

# Build the project using Maven
RUN mvn clean package

# Specify the entry point for the application
CMD ["java", "-cp", "target/classes", "Crawler.Main"]
