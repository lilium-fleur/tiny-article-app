package com.example.demo.subscription;

import com.example.demo.__shared.exception.BadRequestException;
import com.example.demo.subscription.dto.SubscriptionDto;
import com.example.demo.user.User;
import com.example.demo.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserService userService;

    @Transactional
    public SubscriptionDto follow(User currentUser, Long userIdToFollow) {

        if(currentUser.getId().equals(userIdToFollow)) {
            throw new BadRequestException("You can't follow themselves");
        }

        subscriptionRepository.findByFollowerIdAndFollowingId(currentUser.getId(), userIdToFollow)
                .ifPresent(it -> {
                    throw new BadRequestException("You follow this user already");
                });

        User user = userService.findById(userIdToFollow);

        Subscription subscription = Subscription.builder()
                .follower(currentUser)
                .following(user)
                .build();

        return subscriptionMapper.toDto(subscriptionRepository.save(subscription));
    }

    @Transactional
    public void unfollow(User currentUser, Long userIdToUnfollow) {
        Subscription subscription = subscriptionRepository.findByFollowerIdAndFollowingId(currentUser.getId(), userIdToUnfollow)
                .orElseThrow(() -> new BadRequestException("You were not follow this user"));

        subscriptionRepository.delete(subscription);
    }


    @Transactional(readOnly = true)
    public Page<User> getFollowers(Long userId, Pageable pageable) {
        return subscriptionRepository.findAllByFollowingId(userId, pageable)
                .map(Subscription::getFollower);
    }

    @Transactional(readOnly = true)
    public Page<User> getFollowings(Long userId, Pageable pageable) {
        return subscriptionRepository.findAllByFollowerId(userId, pageable)
                .map(Subscription::getFollowing);
    }

}
