-- liquibase formatted sql

-- changeset fleur:1

CREATE TABLE IF NOT EXISTS tokens
(
    id         BIGSERIAL PRIMARY KEY,
    token      VARCHAR(255)                 NOT NULL,
    is_revoked BOOLEAN                      NOT NULL,
    created_at TIMESTAMP                    NOT NULL,
    expires_at TIMESTAMP                    NOT NULL,
    user_session_id    BIGINT REFERENCES user_sessions(id) NOT NULL,
    type       varchar(31)                  NOT NULL
);

-- rollback DROP TABLE tokens

