package com.bkupokry.vkusnoitochka.user;

import com.bkupokry.vkusnoitochka.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

/**
 * Контроллер пользователя
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Запрос о регистрации пользователя
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }

    /**
     * Запрос об авторизации пользователя
     */
    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestParam String username, @RequestParam String password) {
        String loginUser = userService.loginUser(username, password);
        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("token", loginUser);
        return ResponseEntity.ok(loginMap);
    }

    /**
     * Запрос информации о пользователе
     */
    @GetMapping("/get_info")
    public ResponseEntity<User> getUserInfo(@NotNull HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization error");
        }
        String token = authHeader.substring(7);
        String username = jwtTokenProvider.getSubjectFromJWT(token);
        User user = userService.findUser(username);
        return ResponseEntity.ok(user);
    }
}
