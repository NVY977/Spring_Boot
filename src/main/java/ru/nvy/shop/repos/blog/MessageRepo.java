package ru.nvy.shop.repos.blog;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nvy.shop.models.blog.Message;

public interface MessageRepo extends JpaRepository<Message, Long> {

}
