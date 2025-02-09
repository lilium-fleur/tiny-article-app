--liquibase formatted sql


--changeset fleur:1
CREATE TABLE messages
(
    id        BIGSERIAL PRIMARY KEY,
    content   TEXT        NOT NULL,
    sender_id    BIGINT REFERENCES users (id),
    recipient_id BIGINT REFERENCES users (id),
    created_at   TIMESTAMP   NOT NULL,
    chat_id   BIGINT REFERENCES chats (id)
)

--rollback DROP TABLE messages