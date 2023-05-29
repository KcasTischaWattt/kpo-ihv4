package com.bkupokry.vkusnoitochka.security;

import com.bkupokry.vkusnoitochka.user.UserDetailsServiceImplementation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// Так как с JWT токеном мне пришлось очень долго возиться, то я подробно расписал все методы в комментариях
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // провайдер токенов JWT
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // Реализация пользовательских деталей сервиса
    @Autowired
    private UserDetailsServiceImplementation userDetailsServiceImplementation;

    /**
     *  Фильтрация запросов внутри цепочки фильтров
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Извлечение JWT-токена из запроса
        String jwt = extractJwtFromRequest(request);

        // Проверка наличия и валидности JWT-токена
        if (jwt != null && jwtTokenProvider.validateJWTToken(jwt)) {
            // Извлечение имени пользователя из JWT-токена
            String username = jwtTokenProvider.getSubjectFromJWT(jwt);
            // Загрузка деталей пользователя в зависимости от его имени
            UserDetails userDetails = userDetailsServiceImplementation.loadUserByUsername(username);
            // Создание объекта аутентификации с деталями пользователя
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            // Устанавливаем аутентификацию для безопасности
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Отправляем запрос через цепочку фильтров
        filterChain.doFilter(request, response);
    }

    /**
     * Метод извлекает JWT токен из HTTP-запроса
     */
    private @Nullable String extractJwtFromRequest(@NotNull HttpServletRequest request) {
        // Извлекаем заголовка "Authorization" из запроса
        String bearerToken = request.getHeader("Authorization");
        // Проверка наличия и формата токена
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            // Токен возвращается без префикса "Bearer "
            return bearerToken.substring(7);
        }
        // Возвращение токен не найдет - возвращаем null
        return null;
    }
}
