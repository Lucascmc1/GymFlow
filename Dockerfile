FROM maven:3.9.16-eclipse-temurin-21-alpine AS build

WORKDIR /workspace
COPY pom.xml ./
RUN mvn --batch-mode --no-transfer-progress dependency:go-offline
COPY src ./src
RUN mvn --batch-mode --no-transfer-progress package -DskipTests

FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S gymflow && adduser -S gymflow -G gymflow
WORKDIR /app
COPY --from=build /workspace/target/gymflow-1.0.0.jar app.jar

USER gymflow
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
