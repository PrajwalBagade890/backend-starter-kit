# Backend Starter Kit – Book API

A complete **multi-module Spring Boot backend starter repository** containing a Book REST API, PostgreSQL integration, Docker setup, OpenAPI/Swagger documentation, validation, centralized error handling, and a basic application test.

## 1. Project structure

```text
backend-starter-kit/
├── pom.xml                         # Maven parent / multi-module build
├── book-api/                       # Spring Boot application module
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/example/bookapi/
│       │   ├── config/             # OpenAPI configuration
│       │   ├── controller/         # REST endpoints
│       │   ├── dto/                # Request validation
│       │   ├── exception/          # Custom exceptions + global handler
│       │   ├── model/              # JPA entity
│       │   ├── repository/         # Spring Data JPA repository
│       │   ├── service/            # Business logic
│       │   └── BookApiApplication.java
│       └── main/resources/application.yml
├── api/book-api.http               # Sample HTTP requests (IntelliJ/VS Code)
├── Dockerfile
├── docker-compose.yml
├── .dockerignore
└── .gitignore
```

## 2. Prerequisites

- Java Development Kit (JDK) 17 or later
- Apache Maven 3.8+
- Docker Desktop (for container-based run)
- Git (for GitHub upload)

Check installation:

```bash
java -version
mvn -version
docker --version
docker compose version
```

## 3. Run locally with the default H2 database

The default configuration uses an in-memory H2 database, so you do not need to install or configure PostgreSQL for a quick local run.

From the repository root:

```bash
mvn clean install
mvn -pl book-api spring-boot:run
```

Application: `http://localhost:8080`

Swagger UI: `http://localhost:8080/swagger-ui.html`

OpenAPI JSON: `http://localhost:8080/v3/api-docs`

H2 console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:booksdb`, user: `sa`, blank password).

## 4. Run with Docker Compose and PostgreSQL

Run these commands from the repository root (the folder containing `docker-compose.yml`):

```bash
docker compose up --build -d
docker compose ps
docker compose logs -f book-api
```

The API will be available at `http://localhost:8080`. Swagger UI: `http://localhost:8080/swagger-ui.html`.

Stop containers:

```bash
docker compose down
```

Stop containers and remove the database volume (this permanently deletes the local Compose database data):

```bash
docker compose down -v
```

Compose uses development-only credentials. Change them before using this project in a real environment.

## 5. REST API documentation

Base URL: `http://localhost:8080/api/books`

| Method | Endpoint | Description | Success status |
|---|---|---|---|
| GET | `/api/books` | Get all books | 200 OK |
| GET | `/api/books/{id}` | Get one book | 200 OK |
| POST | `/api/books` | Create a book | 201 Created |
| PUT | `/api/books/{id}` | Update a book | 200 OK |
| DELETE | `/api/books/{id}` | Delete a book | 204 No Content |

### Example request body

```json
{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "9780132350884",
  "genre": "Software Engineering",
  "publishedYear": 2008
}
```

`title` and `author` are required. ISBN is optional but must be 10–20 characters containing digits, X, or hyphens when supplied. ISBN values must be unique. Published year, if supplied, must be between 1000 and 2100.

### Example success response

```json
{
  "id": 1,
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "9780132350884",
  "genre": "Software Engineering",
  "publishedYear": 2008,
  "createdAt": "2026-01-01T10:00:00",
  "updatedAt": "2026-01-01T10:00:00"
}
```

### Error responses

Errors return a consistent JSON structure containing `timestamp`, `status`, `error`, `message`, and `path`. Validation errors also include an `errors` object containing field-level messages. Common statuses: `400` invalid request, `404` book not found, `409` duplicate ISBN/conflict, and `500` unexpected server error.

## 6. Test the API

Run tests from the root:

```bash
mvn test
```

A sample request collection is included at `api/book-api.http`. You can open it in IntelliJ IDEA (HTTP Client) or VS Code (REST Client extension) and execute each request.

## 7. Push this project to GitHub

Create an **empty** GitHub repository named `backend-starter-kit` (do not initialize it with a README, license, or `.gitignore`). Then open Git Bash/terminal in this extracted project folder and run:

```bash
git init
git add .
git commit -m "Add complete Spring Boot Book API starter kit"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/backend-starter-kit.git
git push -u origin main
```

Replace `YOUR_USERNAME` with your GitHub username. If `origin` already exists, use:

```bash
git remote set-url origin https://github.com/YOUR_USERNAME/backend-starter-kit.git
git push -u origin main
```

If Git reports that there is no commit yet, run `git add .` and `git commit -m "Initial commit"` before pushing.

## 8. Notes

- This is a starter/learning project, not a production-hardened deployment.
- The Docker Compose credentials are for local development only.
- `ddl-auto: update` is convenient for a starter project; production systems should use database migrations such as Flyway or Liquibase.
- Configure secrets through environment variables in real deployments; do not commit passwords or API keys.
