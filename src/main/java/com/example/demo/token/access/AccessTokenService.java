package com.example.demo.token.access;

import com.example.demo.__shared.jwt.JwtTokenUtil;
import com.example.demo.token.shared.TokenMapper;
import com.example.demo.token.shared.TokenService;
import com.example.demo.token.shared.dto.CreateTokenDto;
import com.example.demo.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccessTokenService extends TokenService<AccessToken> {
    private final JwtTokenUtil jwtTokenUtil;
    private final AccessTokenRepository tokenRepository;

    public AccessTokenService(TokenMapper<AccessToken> tokenMapper,
                              AccessTokenRepository tokenRepository,
                              JwtTokenUtil jwtTokenUtil) {
        super(tokenMapper, tokenRepository);
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected long getExpirationTime() {
        return jwtTokenUtil.getAccessExpiration();
    }

    @Override
    protected String generateToken(User user) {
        return jwtTokenUtil.generateAccessToken(user);
    }

    @Override
    protected AccessToken createTokenEntity(CreateTokenDto dto, LocalDateTime expiresAt) {
        return AccessToken.builder()
                .token(dto.token())
                .expiresAt(expiresAt)
                .userSession(dto.userSession())
                .build();
    }

    @Override
    public boolean validateToken(String accessToken, String fingerprint) {
        AccessToken token = tokenRepository.findByToken(accessToken)
                .orElseThrow(() -> new EntityNotFoundException("Access token not found"));

        return token.getUserSession().getFingerprint().equals(fingerprint)
                && token.getExpiresAt().isAfter(LocalDateTime.now())
                && !token.getIsRevoked();
    }
}
