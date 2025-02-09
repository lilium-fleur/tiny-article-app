package com.example.demo.token.access;

import com.example.demo.token.shared.TokenEntity;
import com.example.demo.usersession.UserSession;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@DiscriminatorValue("ACCESS")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class AccessToken extends TokenEntity {

    @ManyToOne
    @JoinColumn(name = "user_session_id")
    private UserSession userSession;
}
