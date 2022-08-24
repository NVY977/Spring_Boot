package ru.nvy.shop.repos.blog;

import org.springframework.data.repository.CrudRepository;
import ru.nvy.shop.models.blog.Message;

public interface MessageRepo extends CrudRepository<Message, Long> {

}
