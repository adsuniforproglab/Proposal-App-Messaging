# BUILD STAGE
FROM alpine:latest AS build 
WORKDIR /usr/src/app
COPY . .

# RUN STAGE
FROM alpine:latest
WORKDIR /usr/src/app

# Install OpenJDK 21
RUN apk add --no-cache openjdk21

COPY --from=build /usr/src/app/target/proposal-app-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]