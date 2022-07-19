package ru.nvy.shop.repos;

import org.springframework.data.repository.CrudRepository;
import ru.nvy.shop.models.Item;

public interface ItemRepository extends CrudRepository<Item, Long> {
}
