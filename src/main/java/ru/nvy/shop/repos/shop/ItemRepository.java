package ru.nvy.shop.repos.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nvy.shop.models.shop.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
