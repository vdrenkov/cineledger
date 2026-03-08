# CineLedger

API-first, stateless Spring Boot backend for cinema management. The project exposes REST endpoints for a catalog,
scheduling, ordering, reporting, reviews and user/security workflows for external clients (frontend/Postman).

## Tech Stack

- Java 25
- Spring Boot 4.0.3
- Spring Security (JWT + cookie support)
- Spring Data JPA + Hibernate ORM
- Liquibase
- PostgreSQL
- Maven
- JUnit 5 + Mockito + Spring Test (MockMvc)

## Project Structure

- `src/main/resources/db/changelog/`: Liquibase changelogs
- `src/main/resources/DML_Scripts.sql`: Development seed data script
- `src/main/resources/application.yml`: Runtime configuration
- `src/main/resources/CineLedger.postman_collection.json`: API collection

## Architecture

- Layered Spring Boot backend with clear controller, service, repository, mapper, and entity boundaries.
- Controllers expose the HTTP contract, validate input, and delegate orchestration to the service layer.
- Services hold business rules for catalog management, scheduling, ordering, reporting, reviews, and security flows.
- Repositories encapsulate database access through Spring Data JPA against PostgreSQL.
- Manual mappers keep DTO shaping explicit instead of relying on reflection-based mapping.
- Spring Security enforces stateless JWT-based authentication and role-based authorization across the API surface.
- Liquibase owns schema evolution, while Hibernate runs in validation mode to detect entity/schema drift early.

## Environment Variables

Set these before running the application:

- `SPRING_DATASOURCE_URL` (default: `jdbc:postgresql://localhost:5432/cineledger`)
- `SPRING_DATASOURCE_USERNAME` (default: `postgres`)
- `SPRING_DATASOURCE_PASSWORD` (required)
- `JWT_SECRET` (required; use a strong 64+ char secret)
- `APP_BOOTSTRAP_ROLES` (default: `true`)
- `APP_BOOTSTRAP_ADMIN_USERNAME` (optional)
- `APP_BOOTSTRAP_ADMIN_PASSWORD` (optional)
- `APP_BOOTSTRAP_ADMIN_EMAIL` (optional)
- `APP_BOOTSTRAP_ADMIN_FIRST_NAME` (optional; default fallback: `Admin`)
- `APP_BOOTSTRAP_ADMIN_LAST_NAME` (optional; default fallback: `User`)
- `JWT_COOKIE_SECURE` (default: `true`)
- `JWT_COOKIE_SAME_SITE` (default: `Lax`)
- `MAILJET_API_KEY` (optional)
- `MAILJET_API_SECRET` (optional)
- `MAIL_SENDER_EMAIL` (default: `no-reply@cineledger.dev`)
- `MAIL_SENDER_NAME` (default: `CineLedger`)
- `IMDB_API_KEY` (optional)
- `IMDB_API_URL` (default: `https://imdb-api.com/en/API/`)

## Database Setup

1. Create database `cineledger` in PostgreSQL.
2. Start the application once to let Liquibase apply `src/main/resources/db/changelog/changes/001-init-schema.sql`.
3. Run `src/main/resources/DML_Scripts.sql` only for local development/testing seed data.

## Run Locally

1. Run the full verification suite:

```bash
mvn clean verify
```

2. Start the application:

```bash
mvn spring-boot:run
```

3. Import `CineLedger.postman_collection.json` into Postman and test endpoints.

## Current Modules

- Categories
- Cinemas
- Halls
- Items
- Movies
- Programs
- Projections
- Tickets
- Orders and reservations
- Discounts
- Roles
- Users and authentication
- Reviews
- Income/statistics reporting

## Production Readiness Notes

- Uses stateless security configuration.
- `spring.jpa.open-in-view` is disabled.
- Sensitive values are externalized via environment variables.
- Liquibase manages the schema and Hibernate validates it on a startup.
- Liquibase expects a clean `cineledger` database for first startup and applies `001-init-schema.sql`.
- Core roles (`ADMIN`, `VENDOR`, `USER`) can be bootstrapped automatically on a startup.
- An initial admin user can be bootstrapped when `APP_BOOTSTRAP_ADMIN_*` values are provided.
- JWT auth uses the app-specific cookie name `CINELEDGER_AUTH`.
- `GET /csrf` is available for clients that need to fetch the CSRF token explicitly.
- Mailjet and IMDb integrations degrade gracefully when credentials are not configured.
- `spring.jpa.hibernate.ddl-auto` is set to `validate` so entity/schema drift fails fast.
- `DML_Scripts.sql` is development-only and should not be executed in production.
- Full verification passes with `mvn clean verify`.
