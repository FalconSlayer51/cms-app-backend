# Build stage
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copy Gradle files
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Cache dependencies
RUN ./gradlew dependencies --no-daemon || true

# Copy source and build
COPY src src
RUN ./gradlew bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/health || exit 1

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", \
  "app.jar"]