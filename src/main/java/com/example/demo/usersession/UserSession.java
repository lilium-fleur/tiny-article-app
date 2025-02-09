package com.example.demo.usersession;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.demo.user.User;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user_sessions")
@Entity
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime lastActivity;

    @Column(nullable = false)
    private String ipAddress;

    @Column(nullable = false)
    private String deviceInfo;

    @Column(nullable = false)
    private String fingerprint;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isActive = true;

    @JoinColumn(name="user_id")
    @ManyToOne
    private User user;
}
