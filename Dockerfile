# Multi-stage Dockerfile

# Builder: use Gradle's official image to run the project's Gradle wrapper
FROM gradle:8.6-jdk17 AS builder
WORKDIR /home/gradle/project

# Copy wrapper and build files first (cache dependencies)
COPY gradlew gradle/ gradle/wrapper/ build.gradle settings.gradle /home/gradle/project/
RUN chown -R gradle:gradle /home/gradle/project && chmod +x /home/gradle/project/gradlew
USER gradle
RUN ./gradlew --no-daemon --version || true

# Copy source and build fat jar (bootJar)
COPY --chown=gradle:gradle . /home/gradle/project
RUN ./gradlew --no-daemon bootJar -x test

# Runtime: small, maintained JRE image
FROM eclipse-temurin:17-jre-jammy AS runtime
LABEL org.opencontainers.image.source=""

# Create a non-root user for running the app
RUN groupadd -r app && useradd -r -g app app
WORKDIR /app

# Copy the built jar
COPY --from=builder /home/gradle/project/build/libs/*.jar /app/app.jar

# Allow tuning memory and other JVM opts via JAVA_OPTS
ENV JAVA_OPTS="-Xms128m -Xmx512m"

# Expose the application's port
EXPOSE 8081

# Run as non-root user
USER app

# Start command reads configuration from env vars (DB_URL, USERNAME, PASSWORD, etc.)
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
