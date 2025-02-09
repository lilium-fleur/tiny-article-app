package com.example.demo.usersession;

import com.example.demo.token.access.AccessTokenService;
import com.example.demo.token.refresh.RefreshTokenService;
import com.example.demo.user.User;
import com.example.demo.usersession.dto.CreateUserSessionDto;
import com.example.demo.usersession.dto.UserSessionDto;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

@RequiredArgsConstructor
@Service
public class
UserSessionService {

    private final UserSessionRepository userSessionRepository;
    private final UserSessionMapper userSessionMapper;
    private final RefreshTokenService refreshTokenService;
    private final AccessTokenService accessTokenService;


    @Transactional
    public UserSession create(CreateUserSessionDto createUserSessionDto, String fingerprint, HttpServletRequest request){
        UserSession userSession = UserSession.builder()
                .user(createUserSessionDto.user())
                .deviceInfo(getDeviceInfo(request))
                .ipAddress(getClientIpAddress(request))
                .fingerprint(fingerprint)
                .lastActivity(LocalDateTime.now())
                .build();
        return userSessionRepository.saveAndFlush(userSession);

    }


    public void deactivateSession(UserSession userSession){
        userSession.setIsActive(false);

         refreshTokenService.revokeActiveTokens(userSession);
         accessTokenService.revokeActiveTokens(userSession);

         userSessionRepository.saveAndFlush(userSession);

    }

    public void deactivateCurrentSession(User user, String fingerprint){
        userSessionRepository.deactivateSessionByFingerprintAndUser(fingerprint, user);

        refreshTokenService.revokeActiveTokensByUserAndFingerprint(user, fingerprint);
        accessTokenService.revokeActiveTokensByUserAndFingerprint(user, fingerprint);
    }

    @Transactional
    public void deactivateUserSessionById(Long id, User user){
        UserSession userSession = userSessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UserSession not found"));
        userSession.setIsActive(false);

        accessTokenService.revokeActiveTokensByUserAndFingerprint(user, userSession.getFingerprint());
        refreshTokenService.revokeActiveTokensByUserAndFingerprint(user, userSession.getFingerprint());

        userSessionRepository.saveAndFlush(userSession);

    }

    @Transactional
    public void deactivateAllOtherSessions(User user, HttpServletRequest request){
        UserSession userSession = findUserSessionByUser(user, request);
        userSessionRepository.deactivateAllOtherSessions(userSession);
    }

    @Transactional(readOnly = true)
    public Page<UserSessionDto> getUserSessions(Integer page,
                                                Integer size,
                                                String sortDirection,
                                                User curentUser,
                                                HttpServletRequest request){
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection.toUpperCase()), "id");
        return userSessionRepository.findAnotherActiveSessions(pageable, findUserSessionByUser(curentUser, request))
                .map(userSessionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public UserSession findUserSessionByUser(User user, HttpServletRequest request){
        String fingerprint = getFingerprint(request);

        return userSessionRepository.findActiveByFingerprintAndUser(fingerprint, user)
                .orElseThrow(() -> new EntityNotFoundException("UserSession not found"));
    }

    private String getDeviceInfo(HttpServletRequest request){
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));

        String deviceInfo = userAgent.getBrowser().getName();

        if(userAgent.getBrowserVersion() != null){
            deviceInfo += " " + userAgent.getBrowserVersion();
        }

        deviceInfo += " " + userAgent.getOperatingSystem().getName();
        deviceInfo += " " + userAgent.getOperatingSystem().getDeviceType();

        return deviceInfo;
    }



    private String getClientIpAddress(HttpServletRequest request){
        String[] IP_HEADERS = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "HTTP_VIA",
                "REMOTE_ADDR"
        };

        for (String header : IP_HEADERS) {
            String value = request.getHeader(header);

            if(value != null && !value.isEmpty() && !value.equalsIgnoreCase("unknown")){

                String[] parts = value.split("\\s*,\\s*");
                return parts[0];
            }
        }
        return request.getRemoteAddr();
    }

    public String getFingerprint(HttpServletRequest request){
        if(request.getCookies() == null){
            return null;
        }

        Cookie cookies = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals("__paf"))
                .findFirst()
                .orElse(null);

        return cookies == null ? null : cookies.getValue();
    }




}
