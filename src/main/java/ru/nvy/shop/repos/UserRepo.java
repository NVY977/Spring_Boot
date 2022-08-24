package ru.nvy.shop.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nvy.shop.models.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}