package com.example.demo.subscriptions;

import com.example.demo.subscriptions.dto.SubscriptionDto;
import com.example.demo.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(target = "followerId", source = "follower", qualifiedByName = "toId")
    @Mapping(target = "followingId", source = "following", qualifiedByName = "toId")
    SubscriptionDto toDto(Subscription subscription);

    @Named("toId")
    default Long toId(User user) {
        return user.getId();
    }
}
