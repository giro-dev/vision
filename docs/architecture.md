# Architecture (MVP)

## Architectural Style

The MVP backend uses **Hexagonal Architecture** to keep domain logic independent from external technologies.

## Domain Modules

- `camera`: ingestion and camera-related operations
- `face`: face detection/recognition flows
- `vehicle`: vehicle and plate recognition flows
- `event`: normalized detection events
- `training`: incremental training domain logic
- `persistence`: repositories and database mapping

## Ports

- `FaceRecognitionPort`: abstraction over face recognition engine
- `PlateRecognitionPort`: abstraction over license plate engine
- `CameraPort`: abstraction over camera providers/feeds

## Adapters

- `CodeProjectAiAdapter`: implements AI recognition ports
- `ReolinkAdapter`: implements camera ingestion port

## Data Layer

- PostgreSQL as primary relational store
- Flyway for schema versioning
- pgvector planned for embedding similarity in Phase 2

## Testing Baseline

- Unit tests for domain services and use cases
- Integration tests with Testcontainers for persistence and adapter contracts
