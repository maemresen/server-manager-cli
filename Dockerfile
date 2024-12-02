# Use a lightweight JRE base image for Java 21
FROM eclipse-temurin:21-jre

# Set working directory
WORKDIR /app

# Copy the built JAR file into the container
COPY build/libs/server-manager-cli-all.jar app.jar

# Create a directory for the H2 database
RUN mkdir -p /app/data

# Set an environment variable for the H2 database file path
ENV H2_DB_PATH=/app/data/server-db

# Expose the application's default port (change if needed)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]