package com.example.demo.subscriptions;

import com.example.demo.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "subscriptions",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"follower_id", "following_id"}
        )
)
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower; //кто

    @ManyToOne
    @JoinColumn(name = "following_id")
    private User following; //на кого

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
