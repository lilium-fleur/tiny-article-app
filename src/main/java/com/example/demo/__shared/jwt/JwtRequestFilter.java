package com.example.demo.__shared.jwt;

import com.example.demo.token.access.AccessTokenService;
import com.example.demo.user.CustomUserDetails;
import com.example.demo.user.User;
import com.example.demo.usersession.UserSessionService;
import io.jsonwebtoken.JwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Log4j2
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetails customUserDetails;
    private final AccessTokenService accessTokenService;
    private final UserSessionService userSessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String username = null;
        String fingerprint = userSessionService.getFingerprint(request);


        if (request.getRequestURI().contains("/auth/login") ||
                request.getRequestURI().contains("/auth/register") ||
                request.getRequestURI().contains("/auth/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ") || fingerprint == null) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        try {
            username = jwtTokenUtil.getUsername(jwt);
        } catch (Exception e) {
            throw new AccessDeniedException("Invalid JWT");
        }

        if (!StringUtils.isEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {

            User user = customUserDetails.loadUserByUsername(username);

            if (!accessTokenService.validateToken(jwt, fingerprint)) {
                throw new JwtException("Invalid JWT");
            }

            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        filterChain.doFilter(request, response);
    }
}
