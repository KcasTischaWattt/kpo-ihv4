package com.bkupokry.vkusnoitochka.user;

import com.bkupokry.vkusnoitochka.security.JwtTokenProvider;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.http.HttpStatus;

/**
 * Основная бизнес - логика
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    /**
     * Логика регистрации пользователя
     */
    public User registerUser(@NotNull User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This username is already in use");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This email is already in use");
        }

        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }

    /**
     * Логика авторизации пользователя
     */
    public String loginUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid data");
        }
        return tokenProvider.generateJWTToken(username, user.getRole());
    }

    /**
     * Логика поиска пользователя(себя)
     */
    public User findUser(String username) {
        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user;
    }

}
