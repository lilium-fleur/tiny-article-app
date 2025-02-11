package com.example.demo.chat.message;

import com.example.demo.chat.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m " +
            "WHERE m.chat = :chat " +
            "ORDER BY m.id DESC")
    List<Message> findLatestByChat(Chat chat, Pageable pageable);


    @Query("SELECT m FROM Message m " +
            "WHERE m.chat = :chat " +
            "AND m.id < :cursor " +
            "ORDER BY m.id DESC")
    List<Message> findByChatAndIdLessThan(Chat chat, Long cursor, Pageable pageable);

    @Query("SELECT m FROM Message m " +
            "WHERE m.chat = :chat " +
            "AND m.id > :cursor " +
            "ORDER BY m.id")
    List<Message> findByChatAndIdGreaterThan(Chat chat, Long cursor, Pageable pageable);

}
