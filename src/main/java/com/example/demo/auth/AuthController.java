package com.example.demo.auth;


import com.example.demo.auth.dto.AuthDto;
import com.example.demo.auth.dto.LoginDto;
import com.example.demo.auth.dto.RegisterDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<AuthDto> register(
            @Valid @RequestBody RegisterDto registerDto,
            HttpServletResponse response,
            HttpServletRequest request) {
        return ResponseEntity.ok(authService.register(registerDto, response, request));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthDto> login(
            @Valid @RequestBody LoginDto loginDto,
            HttpServletResponse response,
            HttpServletRequest request) {
        return ResponseEntity.ok(authService.login(loginDto, response, request));
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "__tar") String refreshToken,
            @CookieValue(name = "__paf") String fingerprint,
            HttpServletResponse response) {
        authService.logout(refreshToken, fingerprint, response);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/refresh")
    public ResponseEntity<AuthDto> refresh(@CookieValue(name = "__tar") String refreshToken,
                                           @CookieValue(name = "__paf") String fingerprint){
        return ResponseEntity.ok(authService.refreshToken(refreshToken, fingerprint));
    }

}
