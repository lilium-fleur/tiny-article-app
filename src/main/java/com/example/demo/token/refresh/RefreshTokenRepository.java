package com.example.demo.token.refresh;

import com.example.demo.token.shared.TokenRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends TokenRepository<RefreshToken> {


}
