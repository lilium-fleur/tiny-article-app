package com.example.demo.user;

import com.example.demo.auth.dto.RegisterDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import com.example.demo.__shared.exception.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User %s not found", email)));
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User %s not found", username)));
    }

    @Transactional(readOnly = true)
    public User findById(Long id){
        return userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User %s not found", id)));
    }

    @Transactional
    public User createUser(RegisterDto registerDto) {
        userRepository
                .findByEmail(registerDto.email())
                .ifPresent(user -> {
                    throw new BadRequestException("User already exist");
                });

        User user = User.builder()
                .email(registerDto.email())
                .username(registerDto.username())
                .password(passwordEncoder.encode(registerDto.password()))
                .build();

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean matchPassword(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User %s not found", email)));
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id '%d' not found", userId)));

        userRepository.delete(user);
    }

}
