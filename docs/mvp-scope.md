# MVP Scope

## MVP Goal

Deliver a practical first version that proves technical viability and prepares the backend core.

This MVP includes:

- **Phase 0**: Viability spike
- **Phase 1**: Core backend foundation

## Included Deliverables

### 1) Viability Spike (Phase 0)

- Independent proof-of-concept (no Home Assistant integration yet)
- Evaluation dataset targets:
  - 100 known-person images
  - 100 vehicle images
  - 100 night images
- Metrics to measure:
  - Precision
  - Recall
  - Response time

Acceptance targets:

- Person detection > 95%
- Face recognition > 90%
- Plate reading > 90%
- Event processing < 2 seconds

### 2) Core Backend (Phase 1)

- Hexagonal architecture baseline
- Core modules:
  - camera
  - face
  - vehicle
  - event
  - training
  - persistence
- Initial ports:
  - `FaceRecognitionPort`
  - `PlateRecognitionPort`
  - `CameraPort`
- Initial adapters:
  - `CodeProjectAiAdapter`
  - `ReolinkAdapter`

Acceptance targets:

- Test coverage > 80%
- Docker Compose baseline available
- Versioned database schema

## Out of Scope for MVP

The MVP excludes later roadmap phases (Home Assistant component, dashboard, tracking, AI agent, productization).
