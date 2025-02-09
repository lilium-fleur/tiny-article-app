--liquibase formatted sql

-- changeset fleur:1
CREATE TABLE IF NOT EXISTS subscriptions
(
    id           BIGSERIAL PRIMARY KEY,
    follower_id  BIGINT REFERENCES users (id), --кто подлписался
    following_id BIGINT REFERENCES users (id), --на кого подписался
    created_at   TIMESTAMP
)
--rollback DROP TABLE subscriptions