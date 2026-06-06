# Phase 0 – Viability Spike

This directory contains the evaluation scripts for the viability spike.

## Prerequisites

- Docker Compose running with CodeProject.AI (`make up` or `docker compose up -d codeproject-ai`)
- Python 3.10+

## How to run

1. Place test images in `spike/images/`
2. Create `spike/labels.csv` with columns: `filename`, `expected_type`, `expected_label`
   - `expected_type`: `face` or `plate`
3. Run the evaluation:

```bash
make spike
# or manually:
cd spike && pip install -r requirements.txt && python evaluate.py
```

## Example labels.csv

```csv
filename,expected_type,expected_label
person_01.jpg,face,Alice
person_02.jpg,face,Bob
car_01.jpg,plate,ABC1234
night_01.jpg,face,Charlie
```

## Acceptance criteria

| Metric | Target |
|--------|--------|
| Person detection | > 95% |
| Face recognition | > 90% |
| Plate reading | > 90% |
| Event processing | < 2 seconds |
