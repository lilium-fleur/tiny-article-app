package com.example.demo.subscription;

import com.example.demo.subscription.dto.SubscriptionDto;
import com.example.demo.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/{userId}")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionDto> follow(
            @PathVariable Long userId,
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(subscriptionService.follow(user, userId));
    }

    @DeleteMapping
    public ResponseEntity<Void> unfollow(
            @PathVariable Long userId,
            @AuthenticationPrincipal User user){

        subscriptionService.unfollow(user, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/followers")
    public ResponseEntity<Page<User>> getFollowers(
            @PathVariable Long userId,
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(subscriptionService.getFollowers(userId, pageable));
    }

    @GetMapping("/following")
    public ResponseEntity<Page<User>> getFollowing(
            @PathVariable Long userId,
            @PageableDefault Pageable pageable){
        return ResponseEntity.ok(subscriptionService.getFollowings(userId, pageable));
    }
}
