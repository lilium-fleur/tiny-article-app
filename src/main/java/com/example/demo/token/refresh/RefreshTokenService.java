package com.example.demo.token.refresh;

import com.example.demo.__shared.jwt.JwtTokenUtil;
import com.example.demo.token.shared.TokenMapper;
import com.example.demo.token.shared.TokenService;
import com.example.demo.token.shared.dto.CreateTokenDto;
import com.example.demo.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RefreshTokenService extends TokenService<RefreshToken> {
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenRepository tokenRepository;

    public RefreshTokenService(TokenMapper<RefreshToken> tokenMapper,
                               RefreshTokenRepository tokenRepository,
                               JwtTokenUtil jwtTokenUtil) {
        super(tokenMapper, tokenRepository);
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected long getExpirationTime() {
        return jwtTokenUtil.getRefreshExpiration();
    }

    @Override
    protected String generateToken(User user) {
        return jwtTokenUtil.generateRefreshToken(user);
    }

    @Override
    protected RefreshToken createTokenEntity(CreateTokenDto dto, LocalDateTime expiresAt) {
        return RefreshToken.builder()
                .token(dto.token())
                .userSession(dto.userSession())
                .expiresAt(expiresAt)
                .build();
    }

    @Override
    public boolean validateToken(String refreshToken, String fingerprint) {
        RefreshToken token = tokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new EntityNotFoundException("Refresh token not found"));

        return token.getUserSession().getFingerprint().equals(fingerprint)
                && token.getExpiresAt().isAfter(LocalDateTime.now())
                && !token.getIsRevoked();

    }

}
