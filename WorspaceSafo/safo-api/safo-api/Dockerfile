FROM eclipse-temurin:17-jre-alpine
COPY target/safo-api.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]