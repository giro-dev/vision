.PHONY: build test run up down clean spike db-only

## ---- Java / Maven ----

build:                   ## Compile and package (skip tests)
	./mvnw package -DskipTests -B

test:                    ## Run unit + integration tests (Testcontainers)
	./mvnw verify -B

run:                     ## Run locally against a local Postgres (see db-only)
	./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

## ---- Docker Compose ----

up:                      ## Start full stack (Postgres + CodeProject.AI + app)
	docker compose up --build -d

down:                    ## Stop full stack
	docker compose down

clean:                   ## Stop stack and remove volumes
	docker compose down -v

db-only:                 ## Start only Postgres for local dev
	docker compose up -d postgres

## ---- Phase 0 spike ----

spike:                   ## Run evaluation scripts (requires images in spike/images/)
	cd spike && pip install -r requirements.txt -q && python evaluate.py

## ---- Misc ----

swagger:                 ## Open Swagger UI
	@echo "http://localhost:8080/swagger-ui.html"
