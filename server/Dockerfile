# Java runtime
FROM eclipse-temurin:21-jre-alpine
# Working directory
WORKDIR /app
# Copy the Fat JAR
COPY build/libs/*-all.jar /app/tlv-server.jar
# Fallback port
EXPOSE 8080
# Run the server
ENTRYPOINT ["java", "-Dlogback.configurationFile=logback-prod.xml", "-jar", "/app/tlv-server.jar"]
