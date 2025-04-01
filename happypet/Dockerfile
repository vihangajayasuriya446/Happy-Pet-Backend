# Stage 1: Build the application
FROM eclipse-temurin:17-jdk as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build the application (creates /app/target/*.jar)
RUN mvn clean package

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Set the entry point to run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
