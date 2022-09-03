package ru.nvy.shop.repos.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nvy.shop.models.user.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByActivationCode(String code);
}