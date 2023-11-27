# Docker 镜像构建
# @author stars
FROM maven:3.3.9-jdk-8-slim as builder

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build a release artifact.
RUN mvn package -DskipTests

# Run the web service on container startup.
CMD ["java","-jar","/app/target/springboot-init-1.0.0.jar","--spring.profiles.active=prod"]
