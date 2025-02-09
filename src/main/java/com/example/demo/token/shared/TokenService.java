package com.example.demo.token.shared;

import com.example.demo.token.shared.dto.CreateTokenDto;
import com.example.demo.token.shared.dto.TokenDto;
import com.example.demo.user.User;
import com.example.demo.usersession.UserSession;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public abstract class TokenService<T extends TokenEntity> {
    private final TokenMapper<T> tokenMapper;
    private final TokenRepository<T> tokenRepository;

    protected TokenService(TokenMapper<T> tokenMapper, TokenRepository<T> tokenRepository) {
        this.tokenMapper = tokenMapper;
        this.tokenRepository = tokenRepository;
    }

    public TokenDto createToken(CreateTokenDto createTokenDto){
        String jwt = generateToken(createTokenDto.user());

        LocalDateTime expiresAt = LocalDateTime.ofInstant(
                Instant.now().plusMillis(getExpirationTime() * 60 * 1000),
                ZoneId.systemDefault()
        );

        T token = createTokenEntity(
                CreateTokenDto.builder()
                        .userSession(createTokenDto.userSession())
                        .user(createTokenDto.user())
                        .token(jwt)
                        .build(),
                expiresAt);

        return tokenMapper.toDto(tokenRepository.saveAndFlush(token));
    }

    @Transactional
    public void revokeActiveTokens(UserSession userSession){
        tokenRepository.revokeAllActiveTokensByUserSession(userSession);
    }


    public T findByToken(String token){
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Token not found"));
    }

    public void revokeActiveTokensByUserAndFingerprint(User user, String fingerprint) {
        tokenRepository.revokeActiveTokenByFingerprintAndUser(fingerprint, user);
    }

    protected abstract long getExpirationTime();
    protected abstract String generateToken(User user);
    protected abstract T createTokenEntity(CreateTokenDto dto, LocalDateTime expiresAt);
    protected abstract boolean validateToken(String token, String fingerprint);

}
