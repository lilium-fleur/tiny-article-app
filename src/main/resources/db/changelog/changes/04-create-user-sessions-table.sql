
-- liquibase formatted sql

-- changeset fleur:1

CREATE TABLE IF NOT EXISTS user_sessions
(
    id            BIGSERIAL PRIMARY KEY,
    last_activity TIMESTAMP    NOT NULL,
    ip_address    VARCHAR(255) NOT NULL,
    device_info   VARCHAR(255) NOT NULL,
    fingerprint   VARCHAR(255),
    is_active     BOOLEAN      NOT NULL,
    user_id       BIGINT REFERENCES users (id)
);
-- rollback DROP TABLE user_sessions

