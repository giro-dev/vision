# VisionAI for Home Assistant

VisionAI is an open project to recognize people and vehicles from home security cameras and integrate those events with Home Assistant.

## Quick Start

```bash
# Start full stack (Postgres + CodeProject.AI + Spring Boot app)
make up

# Or run locally against a local Postgres
make db-only      # start only Postgres
make run          # run Spring Boot with dev profile

# Run tests
make test

# Swagger UI
open http://localhost:8080/swagger-ui.html
```

## Project Structure

```
├── docker-compose.yml          # Full stack: Postgres, CodeProject.AI, app
├── Dockerfile                  # Multi-stage build for Spring Boot
├── Makefile                    # Developer shortcuts
├── pom.xml                     # Maven build (Java 21, Spring Boot 3.4)
├── src/main/java/dev/giro/vision/
│   ├── VisionApplication.java
│   ├── api/                    # REST controllers
│   ├── camera/                 # Camera module (domain + ports)
│   ├── face/                   # Face detection/recognition (domain + ports)
│   ├── vehicle/                # Vehicle/plate recognition (domain + ports)
│   ├── event/                  # Detection event history (domain + ports)
│   ├── training/               # Incremental training logic
│   ├── persistence/            # JPA entities, repos, Flyway migrations
│   ├── adapter/
│   │   ├── codeprojectai/      # CodeProject.AI face + plate adapter
│   │   └── reolink/            # Reolink camera adapter
│   └── config/                 # Spring config, custom properties
├── src/main/resources/
│   ├── application.yml         # Config with dev/docker/test profiles
│   └── db/migration/           # Flyway SQL migrations
├── src/test/                   # Unit + integration tests (Testcontainers)
├── spike/                      # Phase 0 evaluation scripts (Python)
│   ├── evaluate.py
│   ├── requirements.txt
│   └── README.md
└── docs/                       # Architecture, roadmap, setup guides
```

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/detect/face` | Detect faces in an image (multipart) |
| `POST` | `/api/detect/plate` | Read license plate from image (multipart) |
| `POST` | `/api/persons` | Register a known person |
| `GET` | `/api/persons` | List all persons |
| `GET` | `/api/persons/{id}` | Get person by ID |
| `DELETE` | `/api/persons/{id}` | Delete person |
| `POST` | `/api/cameras` | Register a camera |
| `GET` | `/api/cameras` | List cameras |
| `GET` | `/api/cameras/test?host=` | Test camera connectivity |
| `GET` | `/api/events` | List detection events (filter by `cameraId`, `type`) |
| `GET` | `/actuator/health` | Health check |

## Architecture

Hexagonal (Ports & Adapters) — domain logic is independent from external tech:

- **Ports** define contracts: `FaceRecognitionPort`, `PlateRecognitionPort`, `CameraPort`
- **Adapters** implement them: `CodeProjectAiAdapter`, `ReolinkAdapter`
- **Persistence** bridges domain repositories to Spring Data JPA

## Documentation

- [MVP Scope](docs/mvp-scope.md) — what's included in the MVP
- [Architecture](docs/architecture.md) — hexagonal design and module boundaries
- [Setup Guide](docs/setup.md) — bootstrap order and validation checklist
- [Full Roadmap](docs/roadmap.md) — all 12 phases
- [Spike README](spike/README.md) — Phase 0 evaluation instructions

## Tech Stack

| Component | Technology |
|-----------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3.4 |
| Database | PostgreSQL 16 + pgvector |
| Migrations | Flyway |
| AI Engine | CodeProject.AI Server |
| Camera | Reolink HTTP API |
| Tests | JUnit 5 + Testcontainers |
| API Docs | SpringDoc OpenAPI (Swagger) |
| Build | Maven |
| Containers | Docker Compose |
