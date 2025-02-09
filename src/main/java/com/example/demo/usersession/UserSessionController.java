package com.example.demo.usersession;

import com.example.demo.usersession.dto.UserSessionDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.demo.user.User;

@RestController
@RequestMapping("/api/user-sessions")
@RequiredArgsConstructor
public class UserSessionController {

    private final UserSessionService userSessionService;


    @GetMapping
    public ResponseEntity<Page<UserSessionDto>> getUserSessions(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "DESC") String sortDirection,
            HttpServletRequest request) {
        return ResponseEntity.ok(userSessionService.getUserSessions(page, size, sortDirection, user, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateUserSession(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        userSessionService.deactivateUserSessionById(id, user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deactivateAllOtherUserSessions(
            @AuthenticationPrincipal User user,
            HttpServletRequest request) {
        userSessionService.deactivateAllOtherSessions(user, request);
        return ResponseEntity.noContent().build();
    }




}
