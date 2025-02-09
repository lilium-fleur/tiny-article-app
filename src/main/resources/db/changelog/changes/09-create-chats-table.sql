--liquibase formatted sql


--changeset fleur:1
CREATE TABLE chats
(
    id BIGSERIAL PRIMARY KEY,
    user1_id BIGINT REFERENCES users(id),
    user2_id BIGINT REFERENCES users(id)
)
--rollback DROP TABLE chats