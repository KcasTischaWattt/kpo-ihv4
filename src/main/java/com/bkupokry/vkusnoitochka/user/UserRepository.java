package com.bkupokry.vkusnoitochka.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *  Удобный способ получения объекта пользователя на основе его имени пользователя или электронной почты из базы данных
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User findByEmail(String email);
}
