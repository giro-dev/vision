#!/usr/bin/env python3
"""
Phase 0 – Viability Spike evaluation script.

Measures precision, recall, and response time for:
  - Person / face detection
  - License plate reading

Usage:
  1. Place test images in spike/images/ with ground-truth labels in spike/labels.csv
     (columns: filename, expected_type, expected_label)
  2. Start CodeProject.AI: docker compose up -d codeproject-ai
  3. Run: python spike/evaluate.py
"""

import csv
import os
import sys
import time
from pathlib import Path

import requests
from tabulate import tabulate

CPAI_URL = os.getenv("CPAI_URL", "http://localhost:32168")
IMAGES_DIR = Path(__file__).parent / "images"
LABELS_FILE = Path(__file__).parent / "labels.csv"


def detect_face(image_path: str) -> dict:
    with open(image_path, "rb") as f:
        resp = requests.post(
            f"{CPAI_URL}/v1/vision/face",
            files={"image": (os.path.basename(image_path), f, "image/jpeg")},
            timeout=10,
        )
    return resp.json()


def read_plate(image_path: str) -> dict:
    with open(image_path, "rb") as f:
        resp = requests.post(
            f"{CPAI_URL}/v1/image/alpr",
            files={"image": (os.path.basename(image_path), f, "image/jpeg")},
            timeout=10,
        )
    return resp.json()


def evaluate():
    if not LABELS_FILE.exists():
        print(f"No labels file found at {LABELS_FILE}")
        print("Create labels.csv with columns: filename, expected_type, expected_label")
        print("expected_type: face | plate")
        print("\nExample:")
        print("  person_01.jpg,face,Alice")
        print("  car_01.jpg,plate,ABC1234")
        sys.exit(1)

    results = []
    with open(LABELS_FILE) as f:
        reader = csv.DictReader(f)
        for row in reader:
            filename = row["filename"]
            expected_type = row["expected_type"]
            expected_label = row["expected_label"]
            image_path = IMAGES_DIR / filename

            if not image_path.exists():
                results.append({
                    "file": filename,
                    "type": expected_type,
                    "expected": expected_label,
                    "detected": "FILE_NOT_FOUND",
                    "confidence": 0,
                    "time_ms": 0,
                    "correct": False,
                })
                continue

            start = time.time()
            try:
                if expected_type == "face":
                    resp = detect_face(str(image_path))
                    preds = resp.get("predictions", [])
                    detected = len(preds) > 0
                    confidence = preds[0].get("confidence", 0) if preds else 0
                    label = "face_detected" if detected else "none"
                elif expected_type == "plate":
                    resp = read_plate(str(image_path))
                    preds = resp.get("predictions", [])
                    label = preds[0].get("plate", "") if preds else ""
                    confidence = preds[0].get("confidence", 0) if preds else 0
                else:
                    label = "unknown_type"
                    confidence = 0
            except Exception as e:
                label = f"error: {e}"
                confidence = 0

            elapsed_ms = (time.time() - start) * 1000

            correct = (
                (expected_type == "face" and label == "face_detected")
                or (expected_type == "plate" and label.replace(" ", "").upper() == expected_label.replace(" ", "").upper())
            )

            results.append({
                "file": filename,
                "type": expected_type,
                "expected": expected_label,
                "detected": label,
                "confidence": round(confidence, 3),
                "time_ms": round(elapsed_ms, 1),
                "correct": correct,
            })

    # Summary
    print("\n=== VisionAI Phase 0 – Viability Spike Results ===\n")
    headers = ["File", "Type", "Expected", "Detected", "Confidence", "Time (ms)", "Correct"]
    rows = [[r["file"], r["type"], r["expected"], r["detected"],
             r["confidence"], r["time_ms"], r["correct"]] for r in results]
    print(tabulate(rows, headers=headers, tablefmt="grid"))

    total = len(results)
    correct = sum(1 for r in results if r["correct"])
    avg_time = sum(r["time_ms"] for r in results) / max(total, 1)

    face_results = [r for r in results if r["type"] == "face"]
    plate_results = [r for r in results if r["type"] == "plate"]
    face_correct = sum(1 for r in face_results if r["correct"])
    plate_correct = sum(1 for r in plate_results if r["correct"])

    print(f"\n--- Summary ---")
    print(f"Total:            {total}")
    print(f"Correct:          {correct} / {total} ({100 * correct / max(total, 1):.1f}%)")
    print(f"Face detection:   {face_correct} / {len(face_results)} ({100 * face_correct / max(len(face_results), 1):.1f}%)")
    print(f"Plate reading:    {plate_correct} / {len(plate_results)} ({100 * plate_correct / max(len(plate_results), 1):.1f}%)")
    print(f"Avg response:     {avg_time:.1f} ms")
    print(f"\n--- Acceptance Criteria ---")
    print(f"Person detection > 95%:  {'PASS' if len(face_results) > 0 and face_correct / len(face_results) > 0.95 else 'NEEDS DATA'}")
    print(f"Plate reading > 90%:     {'PASS' if len(plate_results) > 0 and plate_correct / len(plate_results) > 0.90 else 'NEEDS DATA'}")
    print(f"Event processing < 2s:   {'PASS' if avg_time < 2000 else 'FAIL'}")


if __name__ == "__main__":
    evaluate()
