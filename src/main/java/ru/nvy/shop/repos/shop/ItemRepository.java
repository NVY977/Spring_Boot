package ru.nvy.shop.repos.shop;

import org.springframework.data.repository.CrudRepository;
import ru.nvy.shop.models.shop.Item;

public interface ItemRepository extends CrudRepository<Item, Long> {
}
