FROM eclipse-temurin:21-jdk-alpine AS build

RUN apk add --no-cache maven

COPY src /app/src
COPY pom.xml /app

WORKDIR /app
RUN mvn clean package -DskipTests -U && rm -rf /root/.m2 && rm -rf /app/src

# Run stage
FROM eclipse-temurin:21-jre-alpine

# Create non-root user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /usr/src/app

# Copy jar from build stage and set ownership
COPY --from=build /app/target/proposal-app-1.0.jar ./app.jar
RUN chown -R appuser:appgroup /usr/src/app

# Set environment variables
ENV SERVER_PORT=8080

# Switch to non-root user
USER appuser

CMD ["java", "-jar", "app.jar"]

EXPOSE ${SERVER_PORT}
