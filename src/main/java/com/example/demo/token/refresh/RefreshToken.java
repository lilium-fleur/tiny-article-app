package com.example.demo.token.refresh;

import com.example.demo.token.shared.TokenEntity;
import com.example.demo.usersession.UserSession;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Data
@DiscriminatorValue("REFRESH")
@NoArgsConstructor
@Entity
public class RefreshToken extends TokenEntity {

    @OneToOne
    @JoinColumn(name = "user_session_id")
    private UserSession userSession;
}
