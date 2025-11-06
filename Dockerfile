# Multi-stage build for Spring Boot application
# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Temporarily modify pom.xml for Docker build: change packaging to JAR and include Tomcat
# This allows the app to run as an executable JAR in the container
RUN sed -i 's/<packaging>war<\/packaging>/<packaging>jar<\/packaging>/' pom.xml && \
    sed -i 's/<scope>provided<\/scope>/<!-- scope removed for Docker -->/' pom.xml && \
    mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Create a non-root user for security
RUN groupadd -r spring && useradd -r -g spring spring

# Copy the built JAR file from build stage
COPY --from=build /app/target/*.jar app.jar

# Change ownership to non-root user
RUN chown spring:spring app.jar

# Switch to non-root user
USER spring:spring

# Expose port (Render will set PORT env var, but 8080 is default)
EXPOSE 8080

# Run the application
# Render sets PORT env var, so we use it if available, otherwise default to 8080
ENTRYPOINT ["sh", "-c", "java -jar -Dserver.port=${PORT:-8080} app.jar"]