# Step 1: Build the application using Maven
FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app

# Copy only the pom.xml initially to leverage Docker cache
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src /app/src
RUN mvn clean package -DskipTests

# Step 2: Create the final image with OpenJDK and the built JAR
FROM openjdk:17-jdk-alpine
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/*.jar car-service.jar

# Run the application
ENTRYPOINT ["java", "-jar", "car-service.jar"]
