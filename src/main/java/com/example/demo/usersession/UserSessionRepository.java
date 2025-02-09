package com.example.demo.usersession;

import com.example.demo.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {


    @Query("select s from UserSession s " +
            "where s.fingerprint = :fingerprint " +
            "and  s.user = :user " +
            "and s.isActive = true")
    Optional<UserSession> findActiveByFingerprintAndUser(String fingerprint, User user);

    @Query("select s from UserSession s " +
            "where s.user = :#{#userSession.user} " +
            "and s.id != :#{#userSession.id} " +
            "and s.isActive = true")
    Page<UserSession> findAnotherActiveSessions(Pageable pageable, UserSession userSession);


    @Modifying
    @Query("update UserSession s set s.isActive = false " +
            "where s.user = :#{#userSession.user} " +
            "and s.id != :#{#userSession.id}")
    void deactivateAllOtherSessions(UserSession userSession);

    @Modifying
    @Query("update UserSession s set s.isActive = false " +
            "where s.fingerprint = :fingerprint " +
            "and s.user = :user")
    void deactivateSessionByFingerprintAndUser(String fingerprint, User user);

}
