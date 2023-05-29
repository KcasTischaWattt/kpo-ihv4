package com.bkupokry.vkusnoitochka.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурационный класс Spring Security
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfigs {

    /**
     * Защита csrf и доступ к регистрации
     */
    @Bean
    public SecurityFilterChain securityFilterChain(@NotNull HttpSecurity http) throws Exception {
        // Отключаем защиту от CSRF атаки
        http.csrf(csrf -> csrf.disable()).authorizeHttpRequests((requests) -> {
            // Разрешаем доступ для всех запросов
            ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)requests.anyRequest()).permitAll();
        });
        // Включаем форму аутентификации по умолчанию
        http.formLogin(Customizer.withDefaults());

        return http.build();
    }

    /**
     * Конфигурация кодировщика паролей
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Используем BCryptPasswordEncoder для кодирования паролей
        return new BCryptPasswordEncoder();
    }
}