# CineLedger

Spring Boot REST API for cinema management, reservations, concession sales, reviews and reporting.

## Features

- Movie catalogue with title, category, release-date and rating filters.
- Cinema programmes with runtime-based screening overlap checks.
- Customer reservations and staff orders with transactional seat and stock allocation.
- Purchase receipts with quantities, fixed unit prices and allocated discounts.
- Income and sales reports with inclusive date ranges.
- JWT authentication, role-based access, CSRF protection and single-use password recovery tokens.

## Stack

Java 27, Spring Boot 4.1, Spring Security, Spring Data JPA, PostgreSQL 18 and Liquibase.
Maven Wrapper, JUnit, Mockito and Testcontainers support builds and verification.

## Setup

Install JDK 27 and PostgreSQL 18. Create a database named `cineledger`, then set:

| Variable | Value |
| --- | --- |
| `SPRING_DATASOURCE_URL` | Defaults to `jdbc:postgresql://localhost:5432/cineledger`. |
| `SPRING_DATASOURCE_USERNAME` | Defaults to `postgres`. |
| `SPRING_DATASOURCE_PASSWORD` | Your database password. |
| `JWT_SECRET` | A randomly generated secret containing at least 64 UTF-8 bytes. |
| `JWT_COOKIE_SECURE` | Set to `false` for local HTTP; defaults to `true` for HTTPS. |

For the first administrator, set `APP_BOOTSTRAP_ADMIN_USERNAME`,
`APP_BOOTSTRAP_ADMIN_PASSWORD` and `APP_BOOTSTRAP_ADMIN_EMAIL` before starting.
Bootstrap creates missing roles and the configured administrator without replacing existing accounts.
Remove the bootstrap credentials from the environment afterwards.

```sh
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd`. The API runs at `http://localhost:8080`.
Liquibase applies migrations automatically; Hibernate validates the resulting schema.
Check `./mvnw --version` if compilation fails: Maven and the IDE must both use JDK 27,
with preview features disabled.

Optional email delivery uses `MAILJET_API_KEY`, `MAILJET_API_SECRET` and a verified
`MAIL_SENDER_EMAIL`. `MAIL_SENDER_NAME` defaults to `CineLedger`.
Without credentials, email is disabled and recovery tokens are never exposed by the API.
Messages are sent after commit; delivery failures are logged without rolling back saved changes.

## API

Import [CineLedger.postman_collection.json](CineLedger.postman_collection.json).
Set its `baseUrl`, `username` and `password` variables. Run **Get CSRF token**, then **Login**;
Postman keeps the cookies and stores created resource IDs for subsequent requests.
Fill the account variables, including `newUserPassword`, before creating customers or staff.

Clients must retain the cookies from `GET /csrf` and send its JSON `token` as
`X-XSRF-TOKEN` on POST, PUT, PATCH and DELETE requests. Login sets the HttpOnly
`CINELEDGER_AUTH` cookie. Fetch another CSRF token after logout.

| Operation | Access |
| --- | --- |
| Read the catalogue, screening schedules and public reviews | Public. |
| `POST /users` without `roleNames` | Public; creates a `USER` account. |
| `POST /users` with `roleNames` | `ADMIN` only; can create staff accounts. |
| Manage own profile, reservations and reviews | Owner or `ADMIN`. |
| Manage orders, tickets, concessions and discounts | `VENDOR` or `ADMIN`. |
| Manage the catalogue, roles, other accounts and reports | `ADMIN`. |

`POST /password-reset-requests` accepts `{"username":"customer"}` and always returns 202.
The emailed token expires after 30 minutes; requests for the same account have a one-minute cooldown.
`POST /password-resets` accepts `{"token":"…","password":"new password"}` and returns 204 on success.
Tokens are hashed in the database and usable once. Password or profile updates invalidate existing
authentication cookies; log in again afterwards.

Amounts use BGN with two decimal places. Repeated concession IDs in an order represent quantities.
An omitted discount code means no discount. Discounts apply to the order total, rounded to cents;
allocated line amounts add up to that total. Cancellation restores stock and releases seats.
Cinema, hall and movie income includes matching ticket sales; item income covers concessions,
and customer income covers the whole order.

## Verification

Start Docker with Linux containers, then run:

```sh
./mvnw --batch-mode --no-transfer-progress clean verify
```

Unit and controller tests run alongside PostgreSQL integration tests covering authorization,
password recovery, concurrent bookings, stock rollback, scheduling, receipts and reports.
CI runs the same command and scans the packaged runtime dependencies with Trivy.
The combined test coverage report is available at `target/site/jacoco/index.html`.

## Limitations

The API does not process payments or integrate with a cinema operator. Email delivery requires
your own Mailjet account; notifications have no persistent retry queue. Apply HTTPS, request
limits and database backups when exposing an instance publicly.

## Acknowledgments

This is a personal portfolio project and is not for redistribution.
