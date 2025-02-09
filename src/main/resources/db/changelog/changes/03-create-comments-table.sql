-- liquibase formatted sql

-- changeset fleur:1

CREATE TABLE IF NOT EXISTS comments
(
    id         BIGSERIAL PRIMARY KEY,
    comment    TEXT                            NOT NULL,
    created_at TIMESTAMP                       NOT NULL,
    article_id BIGINT REFERENCES articles (id) NOT NULL,
    user_id    BIGINT REFERENCES users (id)    NOT NULL,
    comment_id BIGINT REFERENCES comments (id)
);

-- rollback DROP TABLE comments
