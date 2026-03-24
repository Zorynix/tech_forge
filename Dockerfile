# --- Stage 1: Build ---
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY gradlew settings.gradle build.gradle ./
COPY gradle/ gradle/

COPY api-gateway/build.gradle api-gateway/build.gradle
COPY product-service/build.gradle product-service/build.gradle
COPY auth-service/build.gradle auth-service/build.gradle
COPY order-service/build.gradle order-service/build.gradle
COPY notification-service/build.gradle notification-service/build.gradle


RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true

COPY api-gateway/src api-gateway/src
COPY product-service/src product-service/src
COPY auth-service/src auth-service/src
COPY order-service/src order-service/src
COPY notification-service/src notification-service/src


ARG SERVICE_NAME
RUN ./gradlew :${SERVICE_NAME}:bootJar -x test --no-daemon

# --- Stage 2: Runtime ---
FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

ARG SERVICE_NAME
COPY --from=builder /app/${SERVICE_NAME}/build/libs/*.jar app.jar

RUN chown -R appuser:appgroup /app
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
