package com.example.demo.subscription;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

        Page<Subscription> findAllByFollowerId(Long userId, Pageable pageable);
        Page<Subscription> findAllByFollowingId(Long userId, Pageable pageable);

        Optional<Subscription> findByFollowerIdAndFollowingId(Long follower, Long following);

}
