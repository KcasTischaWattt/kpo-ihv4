package com.bkupokry.vkusnoitochka.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;


// Так как с JWT токеном мне пришлось очень долго возиться, то я подробно расписал все методы в комментариях
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecretKey;

    @Value("${jwt.expiration}")
    private int jwtExpirationTime;

    /**
     * Генерирация JWT токена
     */
    public String generateJWTToken(String username, String role) {
        UserDetails userDetails = User.withUsername(username).password("").roles(role).build();

        // Текущая дата нам нужна для установки срока действия токена
        Date date = new Date();

        // "Постройка" JWT токена
        String token = Jwts.builder()
                .setSubject(userDetails.getUsername()) // Субъект токена - имя пользователя.
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + jwtExpirationTime)) // Устанавливаем дату истечения срока действия токена
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey) // Формируем токен указанным алгоритмом и секретным ключом.
                .compact();

        return token;
    }

    /**
     * Получение имя пользователя из указанного JWT токена.
     */
    public String getSubjectFromJWT(String token) {
        // Парсинг JWT токена и извлечение данных
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecretKey) // Ключ для проверки подлинности
                .parseClaimsJws(token) // Парсинг токена
                .getBody();

        return claims.getSubject();
    }

    /**
     * Валидация указанного JWT токена
     */
    public boolean validateJWTToken(String authToken) {
        try {
            // Попытка распарсить и проверить валидность токена
            Jwts.parserBuilder().build().parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}