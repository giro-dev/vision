# VisionAI for Home Assistant - Full Roadmap

## Phase 0 - Viability Spike

### Goal

Validate selected technologies can recognize:

- Known persons
- European license plates
- Images from Reolink cameras

### Architecture

Standalone POC, without Home Assistant.

### Technologies

- Docker Compose
- CodeProject.AI
- PostgreSQL
- Python evaluation scripts

### Implementation

Collect:

- 100 known-person images
- 100 vehicle images
- 100 night images

Measure:

- Precision
- Recall
- Response time

### Acceptance Criteria

- >95% person detection
- >90% face recognition
- >90% plate reading
- <2s per event

---

## Phase 1 - Core Backend

### Goal

Build the central domain.

### Architecture

Hexagonal Architecture.

Modules:

- camera
- face
- vehicle
- event
- training
- persistence

### Technologies

- Java 21
- Spring Boot
- PostgreSQL
- Flyway
- Testcontainers

### Implementation

Ports:

- `FaceRecognitionPort`
- `PlateRecognitionPort`
- `CameraPort`

Adapters:

- `CodeProjectAiAdapter`
- `ReolinkAdapter`

### Acceptance Criteria

- Coverage >80%
- Functional Docker Compose
- Versioned database

---

## Phase 2 - Face Recognition

### Goal

Identify known persons.

### Architecture

Embedding-based recognition.

### Technologies

- InsightFace
- pgvector

### Implementation Pipeline

1. Detect face
2. Generate embedding
3. Similarity search
4. Return identity

### Data Model (`face`)

- id
- person_id
- embedding
- image_url

### Acceptance Criteria

- Match <300ms
- Precision >90%
- Support at least 1000 faces

---

## Phase 3 - Vehicle Recognition

### Goal

Identify vehicles.

### Technologies

- YOLOv8
- OCR
- LPR

### Implementation Pipeline

1. Detect vehicle
2. Crop
3. Detect plate
4. OCR
5. Match

### Acceptance Criteria

- >90% correct readings
- <500ms

---

## Phase 4 - Person Management

### Goal

Create and manage identities.

### API

- `POST /persons`
- `GET /persons`
- `PUT /persons/{id}`

### Features

- Create person
- Edit person
- Merge persons
- Delete person

### Acceptance Criteria

- Complete CRUD
- Validations
- Tests

---

## Phase 5 - Incremental Training

### Goal

Enable training without full model retraining.

### Implementation

When an unknown face is assigned:

1. Create embedding
2. Link to person
3. Update vector index

### Architecture

Event-driven via `UnknownFaceAssignedEvent`.

### Acceptance Criteria

- New training visible immediately
- No service restart required

---

## Phase 6 - Home Assistant Integration

### Goal

Native integration.

### Technologies

- Home Assistant Custom Component
- Config Flow
- Zeroconf

### Entities

- `sensor.last_person`
- `sensor.last_vehicle`
- `sensor.unknown_faces`

### Events

- `visionai.person_detected`
- `visionai.vehicle_detected`
- `visionai.unknown_face`

### Acceptance Criteria

- Automatic discovery
- Installable via HACS

---

## Phase 7 - Unknown Faces Dashboard

### Goal

Manage training from Home Assistant.

### Implementation

Custom panel with:

- Gallery
- Assignment
- Ignore
- Merge

### Acceptance Criteria

- No backend access required
- Entire workflow from HA

---

## Phase 8 - Tracking

### Goal

Avoid redundant processing.

### Technologies

- ByteTrack

### Implementation

For a detected person (e.g., Track #12), compute embeddings only at:

- Track start
- Significant track changes

### Acceptance Criteria

- >70% reduction in face inferences

---

## Phase 9 - Person-Vehicle Association

### Goal

Learn relations.

### Implementation

Correlation engine rule:

If person and vehicle repeatedly appear together, generate suggestion.

### Acceptance Criteria

- Suggestions with score
- Manual confirmation

---

## Phase 10 - AI Agent

### Goal

Add intelligent capabilities.

### Technologies

- Spring AI
- MCP
- OpenAI-compatible APIs

### Tools

- `findPerson()`
- `findVehicle()`
- `searchEvents()`

### Use Cases

- "Who arrived today?"
- "When did Albert's car appear?"
- "Show unknown faces from this week"

### Acceptance Criteria

- Correct natural-language query responses

---

## Phase 11 - Productization

### Goal

Prepare publication.

### Implementation

- Multi-language support
- Optional telemetry
- Backups
- Updates

### Distribution

- Docker Hub
- HACS
- GitHub Releases

### Acceptance Criteria

- Installable in <15 minutes
- Complete documentation

---

## Phase 12 - Official Home Assistant Publication

### Goal

Enter the official catalog.

### Requirements

- Config Flow
- Tests
- Translations
- Quality Scale Bronze+

### Acceptance Criteria

- PR accepted
- Integration published
