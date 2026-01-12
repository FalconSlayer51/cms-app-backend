# Multi-stage Dockerfile

# Builder: use Gradle image with Gradle 9 to satisfy Spring Boot plugin requirements
FROM gradle:9-jdk17 AS builder
WORKDIR /home/gradle/project

# Copy wrapper and build files first (cache dependencies)
COPY gradlew gradle/ gradle/wrapper/ build.gradle settings.gradle /home/gradle/project/
# Ensure gradle cache dir exists and set ownership; make wrapper executable
RUN mkdir -p /home/gradle/.gradle && chown -R gradle:gradle /home/gradle && chown -R gradle:gradle /home/gradle/project && chmod +x /home/gradle/project/gradlew || true

USER gradle
# If gradle-wrapper.jar is missing, run gradle wrapper to generate it; otherwise print version
RUN if [ -f /home/gradle/project/gradle/wrapper/gradle-wrapper.jar ]; then ./gradlew --no-daemon --version || true; else gradle wrapper || true; fi

# Copy source and build fat jar (bootJar). Use ownership flag so files inside container are owned by gradle user
COPY --chown=gradle:gradle . /home/gradle/project
# Ensure wrapper is executable even if host filesystem lost executable bit
RUN chmod +x /home/gradle/project/gradlew || true
# Run build as gradle user
RUN ./gradlew --no-daemon bootJar -x test

# Runtime: small, maintained JRE image
FROM eclipse-temurin:17-jre-jammy AS runtime
LABEL org.opencontainers.image.source=""

# Create a non-root user for running the app
RUN groupadd -r app && useradd -r -g app app
# Create app dir and ensure ownership so non-root user can read/write as needed
RUN mkdir -p /app && mkdir -p /app/tmp /app/logs && chown -R app:app /app
WORKDIR /app

# Copy the built jar and make sure it's owned by the non-root user and readable
COPY --from=builder /home/gradle/project/build/libs/*.jar /app/app.jar
RUN chown app:app /app/app.jar && chmod 644 /app/app.jar || true

# Allow tuning memory and other JVM opts via JAVA_OPTS and set tempdir for non-root
ENV JAVA_OPTS="-Xms128m -Xmx512m -Djava.io.tmpdir=/app/tmp"

# Expose the application's port
EXPOSE 8081

# Run as non-root user
USER app

# Start command reads configuration from env vars (DB_URL, USERNAME, PASSWORD, etc.)
# Bind to platform-provided $PORT when available (Render sets PORT). Fallback to 8081.
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -Dserver.port=${PORT:-8081} -jar /app/app.jar"]
