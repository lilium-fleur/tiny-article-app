package com.example.demo.token.access;

import com.example.demo.token.shared.TokenRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessTokenRepository extends TokenRepository<AccessToken> {
}
