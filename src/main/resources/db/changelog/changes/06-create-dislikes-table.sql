
-- liquibase formatted sql

-- changeset fleur:1

CREATE TABLE IF NOT EXISTS dislikes
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT REFERENCES users (id)                      NOT NULL,
    article_id BIGINT REFERENCES articles (id) ON DELETE CASCADE NOT NULL
);
-- rollback DROP TABLE dislikes
