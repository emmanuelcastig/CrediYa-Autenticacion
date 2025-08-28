FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app

COPY applications/app-service/build/libs/*.jar Autenticacion.jar

USER appuser

EXPOSE 9001

ENTRYPOINT ["java", "-jar", "Autenticacion.jar"]