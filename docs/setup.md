# Setup Guide (MVP Bootstrap)

## Planned Tech Stack (MVP)

### Phase 0 (Viability Spike)

- Docker Compose
- CodeProject.AI
- PostgreSQL
- Python evaluation scripts

### Phase 1 (Core Backend)

- Java 25
- Spring Boot 4
- Spring AI
- PostgreSQL + pgvector
- Flyway
- Testcontainers
- Docker Compose

## Suggested Bootstrap Order

1. Create backend skeleton (Spring Boot 4, Java 25)
2. Add module packages (`camera`, `face`, `vehicle`, `event`, `training`, `persistence`)
3. Define core ports and use-case services
4. Add PostgreSQL + Flyway migrations
5. Add Testcontainers integration tests
6. Add adapters for CodeProject.AI and Reolink

## MVP Validation Checklist

- [ ] Reproducible local environment
- [ ] Database schema under version control
- [ ] Port interfaces and adapter contracts defined
- [ ] Baseline tests in place
- [ ] Initial metrics collection flow for Phase 0

## Documentation Pointers

- MVP scope: [mvp-scope.md](mvp-scope.md)
- Architecture: [architecture.md](architecture.md)
- Full roadmap: [roadmap.md](roadmap.md)
