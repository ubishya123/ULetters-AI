# Stage 1: Build the application using Maven
FROM eclipse-temurin:17-jdk-jammy as builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the final, smaller image to run the application
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Copy the built JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar
# Expose the port Spring Boot runs on
EXPOSE 8080
# Command to run the application
ENTRYPOINT ["java","-jar","app.jar"]