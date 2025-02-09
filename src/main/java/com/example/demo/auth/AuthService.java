package com.example.demo.auth;

import com.example.demo.auth.dto.AuthDto;
import com.example.demo.auth.dto.LoginDto;
import com.example.demo.auth.dto.RegisterDto;
import com.example.demo.__shared.jwt.JwtTokenUtil;
import com.example.demo.token.access.AccessTokenService;
import com.example.demo.token.refresh.RefreshToken;
import com.example.demo.token.refresh.RefreshTokenService;
import com.example.demo.token.shared.dto.CreateTokenDto;
import com.example.demo.token.shared.dto.TokenDto;
import com.example.demo.user.User;
import com.example.demo.user.UserMapper;
import com.example.demo.user.UserService;
import com.example.demo.usersession.UserSession;
import com.example.demo.usersession.UserSessionService;
import com.example.demo.usersession.dto.CreateUserSessionDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AccessTokenService accessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserSessionService userSessionService;


    @Transactional
    public AuthDto register(RegisterDto registerDto, HttpServletResponse response, HttpServletRequest request) {
        saveFingerprintCookie(response, registerDto.fingerprint());

        User user = userService.createUser(registerDto);
        TokenDto accessToken = createAuthenticationSession(user, registerDto.fingerprint(), response, request);

        return AuthDto.builder()
                .userDto(userMapper.toDto(user))
                .token(accessToken)
                .build();
    }

    @Transactional
    public AuthDto login(LoginDto loginDto, HttpServletResponse response, HttpServletRequest request) {
        if(!userService.matchPassword(loginDto.email(), loginDto.password())){
            throw new AccessDeniedException("Invalid password or email");
        }

        saveFingerprintCookie(response, loginDto.fingerprint());

        User user = userService.findByEmail(loginDto.email());

        userSessionService.deactivateCurrentSession(user, loginDto.fingerprint());

        TokenDto accessToken = createAuthenticationSession(user, loginDto.fingerprint(), response, request);

        return AuthDto.builder()
                .userDto(userMapper.toDto(user))
                .token(accessToken)
                .build();
    }

    @Transactional
    public void logout(String refreshToken, String fingerprint, HttpServletResponse response) {
        RefreshToken refreshTokenEntity = refreshTokenService.findByToken(refreshToken);

        if(refreshTokenService.validateToken(refreshToken, fingerprint)){
            userSessionService.deactivateSession(refreshTokenEntity.getUserSession());

            throw new AccessDeniedException("Invalid refresh token");
        }

        userSessionService.deactivateSession(refreshTokenEntity.getUserSession());

        ResponseCookie refreshTokenCookie = ResponseCookie.from("__tar", "")
                .httpOnly(true)
                .maxAge(0)
                .secure(false)
                .path("/api/auth")
                .sameSite("Lax")
                .build();

        ResponseCookie fingerprintCookie = ResponseCookie.from("__paf", "")
                .httpOnly(true)
                .maxAge(0)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, fingerprintCookie.toString());
    }

    @Transactional
    public AuthDto refreshToken(String refreshToken, String fingerprint) {
        RefreshToken refreshTokenEntity = refreshTokenService.findByToken(refreshToken);

        if(!refreshTokenService.validateToken(refreshToken, fingerprint)){
            userSessionService.deactivateSession(refreshTokenEntity.getUserSession());

            throw new AccessDeniedException("Invalid refresh token");
        }

        accessTokenService.revokeActiveTokens(refreshTokenEntity.getUserSession());

        CreateTokenDto createTokenDto = CreateTokenDto.builder()
                .userSession(refreshTokenEntity.getUserSession())
                .user(refreshTokenEntity.getUserSession().getUser())
                .build();

        TokenDto accessTokenDto = accessTokenService.createToken(createTokenDto);

        return AuthDto.builder()
                .userDto(userMapper.toDto(refreshTokenEntity.getUserSession().getUser()))
                .token(accessTokenDto)
                .build();

        }


    private TokenDto createAuthenticationSession(User user, String fingerprint, HttpServletResponse response, HttpServletRequest request) {

        CreateUserSessionDto createUserSessionDto = CreateUserSessionDto.builder()
                .user(user)
                .build();
        UserSession userSession = userSessionService.create(createUserSessionDto, fingerprint, request);

        accessTokenService.revokeActiveTokens(userSession);

        CreateTokenDto createTokenDto = CreateTokenDto.builder()
                .userSession(userSession)
                .user(user)
                .build();

        TokenDto accessTokenDto = accessTokenService.createToken(createTokenDto);
        TokenDto refreshTokenDto = refreshTokenService.createToken(createTokenDto);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("__tar", refreshTokenDto.token())
                .httpOnly(true)
                .secure(false)
                .path("/api/auth")
                .sameSite("Lax")
                .maxAge(jwtTokenUtil.getRefreshExpiration())
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return accessTokenDto;
    }

    private void saveFingerprintCookie(HttpServletResponse response, String fingerprint){
        ResponseCookie fingerprintCookie = ResponseCookie.from("__paf", fingerprint)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(jwtTokenUtil.getRefreshExpiration())
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, fingerprintCookie.toString());
    }



}
