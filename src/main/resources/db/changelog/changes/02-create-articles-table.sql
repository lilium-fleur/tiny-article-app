-- liquibase formatted sql

-- changeset fleur:1

CREATE TABLE IF NOT EXISTS articles
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255),
    content     TEXT,
    comment     TEXT,
    original_id BIGINT REFERENCES articles (id),
    author_id   BIGINT REFERENCES users (id) NOT NULL,
    created_at  TIMESTAMP                    NOT NULL,
    state       VARCHAR(31)                  NOT NULL
);
-- rollback DROP TABLE articles
