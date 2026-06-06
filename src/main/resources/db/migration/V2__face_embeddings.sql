-- Phase 2: add embedding column for face similarity search
-- Uses pgvector when available, falls back to bytea otherwise

DO $$
BEGIN
    CREATE EXTENSION IF NOT EXISTS vector;
    ALTER TABLE faces ADD COLUMN embedding vector(512);
    CREATE INDEX idx_faces_embedding ON faces USING ivfflat (embedding vector_cosine_ops)
        WITH (lists = 10);
EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'pgvector not available — using bytea for embeddings';
        BEGIN
            ALTER TABLE faces ADD COLUMN embedding bytea;
        EXCEPTION
            WHEN duplicate_column THEN NULL;
        END;
END
$$;
