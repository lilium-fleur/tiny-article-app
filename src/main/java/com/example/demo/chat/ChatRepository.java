package com.example.demo.chat;

import com.example.demo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findByUser1AndUser2(User user1, User user2);

    @Query("SELECT c FROM Chat c " +
            "WHERE c.user1 = :user " +
            "OR c.user2 = :user")
    List<Chat> findAllByUser(User user);

}
