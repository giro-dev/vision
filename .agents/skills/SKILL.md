---
name: visionai-dev
description: Development guide for VisionAI — computer vision backend for Home Assistant. Covers stack, architecture, testing, and common workflows.
---

# VisionAI Development Guide

## Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| Language | Java | 21 (LTS) |
| Framework | Spring Boot | 3.4.x |
| Database | PostgreSQL | 16 + pgvector |
| Migrations | Flyway | managed by Spring Boot |
| AI Engine | CodeProject.AI Server | latest |
| Camera | Reolink API | HTTP / CGI |
| Build | Maven (wrapper) | 3.9.x |
| Tests | JUnit 5 + Testcontainers | — |
| API Docs | SpringDoc OpenAPI | 2.7.x |
| Containers | Docker Compose | v2 |

## Architecture

Hexagonal (Ports & Adapters):

```
api/             → REST controllers (Spring MVC)
  ├── DetectionController   POST /api/detect/face, /api/detect/plate
  ├── PersonController      CRUD /api/persons
  ├── CameraController      POST/GET /api/cameras
  └── EventController       GET /api/events

{module}/domain/ → Domain models + services (pure Java, no framework deps)
{module}/port/   → Port interfaces (repository + external service contracts)

adapter/         → External service adapters
  ├── codeprojectai/  → CodeProjectAiAdapter (face + plate recognition)
  └── reolink/        → ReolinkAdapter (camera snapshots)

persistence/     → JPA entities + repository adapters bridging ports to Spring Data
  ├── entity/    → JPA @Entity classes
  └── jpa/       → Spring Data JpaRepository interfaces

config/          → Spring configuration, custom properties
```

### Modules

- **camera** — camera registration, snapshot capture
- **face** — face detection, embedding, person management
- **vehicle** — vehicle detection, plate OCR
- **event** — normalized detection event history
- **training** — incremental training (add faces to known persons)
- **persistence** — JPA layer, Flyway migrations

## Quick Start

```bash
# Start only Postgres (for local dev)
make db-only

# Run the app locally
make run

# Run full stack (Postgres + CodeProject.AI + app)
make up

# Run all tests (unit + integration via Testcontainers)
make test

# Build JAR (no tests)
make build

# Run Phase 0 spike evaluation
make spike

# Swagger UI
open http://localhost:8080/swagger-ui.html
```

## Testing

### Unit tests
Domain services are tested with in-memory stub implementations of ports.
No Spring context needed. Fast.

```bash
./mvnw test
```

### Integration tests (Testcontainers)
Persistence tests use `@DataJpaTest` + `@ActiveProfiles("test")`.
The `test` profile uses Testcontainers JDBC URL (`jdbc:tc:postgresql:16-alpine:///visionai`).
Docker must be running.

```bash
./mvnw verify
```

### Controller tests
Use `@WebMvcTest` with `@MockBean` for service dependencies. No DB needed.

## Database

- Migrations live in `src/main/resources/db/migration/`
- Naming: `V{n}__{description}.sql`
- JPA `ddl-auto=validate` — Hibernate validates schema against entities but never creates/alters tables
- pgvector extension is available for future embedding columns

## Docker Compose

Services:
- **postgres**: pgvector/pgvector:pg16 on port 5432 (user/pass/db: `visionai`)
- **codeproject-ai**: codeproject/ai-server on port 32168
- **app**: Spring Boot on port 8080

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/detect/face` | Detect faces (multipart image) |
| POST | `/api/detect/plate` | Read license plate (multipart image) |
| POST | `/api/persons` | Register person |
| GET | `/api/persons` | List persons |
| GET | `/api/persons/{id}` | Get person |
| DELETE | `/api/persons/{id}` | Delete person |
| POST | `/api/cameras` | Register camera |
| GET | `/api/cameras` | List cameras |
| GET | `/api/cameras/test?host=` | Test camera reachability |
| GET | `/api/events` | List events (filter by cameraId, type) |
| GET | `/actuator/health` | Health check |

## Configuration

Key properties (override via env vars or `application.yml`):

```yaml
vision:
  codeproject-ai:
    url: http://localhost:32168    # VISION_CODEPROJECT_AI_URL
  reolink:
    default-host: ""               # VISION_REOLINK_DEFAULT_HOST
    default-user: ""               # VISION_REOLINK_DEFAULT_USER
    default-password: ""           # VISION_REOLINK_DEFAULT_PASSWORD
```

## Phase 0 Spike

The `spike/` directory contains Python scripts to benchmark detection quality.
See `spike/README.md` for details on running evaluations.

## Conventions

- Records for immutable domain models
- Domain services are `@Service`-annotated, depend only on port interfaces
- Repository adapters in `persistence/` bridge port interfaces to JPA
- No Lombok — use records and explicit constructors
- Profiles: `dev` (local), `docker` (compose), `test` (Testcontainers)
