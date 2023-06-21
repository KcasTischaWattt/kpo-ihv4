package com.vkusnoitochka.ordermanagment.security;

import com.vkusnoitochka.ordermanagment.handleservice.HandleTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Класс, отвечающий за фильтрацию запросов и валидацию JWT токенов
 * Наследует класс OncePerRequestFilter, благодаря чему вызывается лишь раз за запрос
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // Сервис JWT
    private final JwtService jwtService;
    // Вспомогательный сервис для пользователя
    private final HandleTokenService handleTokenService;

    @SneakyThrows
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String user = handleTokenService.handleToken(jwt);
            String res = user.substring(user.indexOf("\"email\":") + 9, user.indexOf(",\"password\"") - 1);
            if (!jwtService.isTokenValid(jwt, res)) {
                throw new ServletException("You have to be authenticated!");
            }
            filterChain.doFilter(request, response);
        }
    }
}