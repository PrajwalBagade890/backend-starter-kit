# Build from the repository root: docker build -t backend-starter-kit .
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml ./
COPY book-api/pom.xml book-api/pom.xml
RUN mvn -B -q -pl book-api -am dependency:go-offline
COPY book-api/src book-api/src
RUN mvn -B -pl book-api -am clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
RUN useradd --system --uid 10001 spring
COPY --from=build /workspace/book-api/target/book-api-1.0.0.jar app.jar
USER 10001
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
