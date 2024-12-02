FROM eclipse-temurin:21-jre

WORKDIR /app
COPY build/libs/server-manager-cli-all.jar app.jar

RUN mkdir -p /app/data
ENV JDBC="jdbc:h2:file:/app/data/server-db;AUTO_SERVER=TRUE"

ENTRYPOINT ["java", "-jar", "app.jar"]