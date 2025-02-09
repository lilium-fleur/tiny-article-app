package com.example.demo.user;

import com.example.demo.__shared.exception.BadRequestException;
import com.example.demo.auth.dto.RegisterDto;
import com.example.demo.content.shared.ArticleRepository;
import com.example.demo.content.shared.BaseArticle;
import com.example.demo.content.shared.dto.ArticleDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void createUser_savesUser() {
        RegisterDto registerDto = RegisterDto.builder()
                .email("asd@gmail.com")
                .username("test")
                .password("test")
                .fingerprint("test")
                .build();

        User user = User.builder()
                .email("asd@gmail.com")
                .username("test")
                .password("test")
                .build();

        Mockito.when(userRepository.findByEmail(registerDto.email())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(registerDto.password())).thenReturn("test");
        Mockito.when(userRepository.save(user)).thenReturn(user);

        userService.createUser(registerDto);


        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    void createUser_throwsException() {
        RegisterDto registerDto = RegisterDto.builder()
                .email("asd@gmail.com")
                .username("test")
                .password("test")
                .fingerprint("test")
                .build();
        User user = User.builder()
                .id(1L)
                .email("asd@gmail.com")
                .username("test")
                .password("test")
                .build();

        Mockito.when(userRepository.findByEmail(registerDto.email())).thenReturn(Optional.of(user));

        Assertions.assertThrows(BadRequestException.class, () -> userService.createUser(registerDto));
    }

    @Test
    void deleteUser_delete(){
        Long userId = 1L;

        User user = new User();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deleteUser(userId);
        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
    }

    @Test
    void deleteUser_throwsException() {
        Long userId = 1L;

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(userId));
    }


    @Test
    void matchPassword_throwsException() {
        String email = "asd@gmail.com";
        String password = "test";
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> userService.matchPassword(email, password));

    }
}