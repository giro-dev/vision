-- VisionAI initial schema

CREATE TABLE cameras (
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    host        VARCHAR(255) NOT NULL,
    type        VARCHAR(50)  NOT NULL,
    enabled     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE TABLE persons (
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE TABLE faces (
    id          UUID PRIMARY KEY,
    person_id   UUID REFERENCES persons(id) ON DELETE CASCADE,
    image_url   VARCHAR(1024),
    confidence  DOUBLE PRECISION NOT NULL DEFAULT 0,
    detected_at TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE TABLE vehicles (
    id          UUID PRIMARY KEY,
    plate       VARCHAR(20) UNIQUE,
    color       VARCHAR(50),
    type        VARCHAR(50),
    confidence  DOUBLE PRECISION NOT NULL DEFAULT 0,
    detected_at TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE TABLE detection_events (
    id          UUID PRIMARY KEY,
    camera_id   UUID REFERENCES cameras(id) ON DELETE SET NULL,
    type        VARCHAR(50)  NOT NULL,
    label       VARCHAR(255),
    confidence  DOUBLE PRECISION NOT NULL DEFAULT 0,
    image_url   VARCHAR(1024),
    timestamp   TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_events_camera  ON detection_events(camera_id);
CREATE INDEX idx_events_type    ON detection_events(type);
CREATE INDEX idx_events_ts      ON detection_events(timestamp);
CREATE INDEX idx_faces_person   ON faces(person_id);
CREATE INDEX idx_vehicles_plate ON vehicles(plate);
