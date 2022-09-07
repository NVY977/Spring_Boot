package ru.nvy.shop.service.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.nvy.shop.models.shop.Item;
import ru.nvy.shop.repos.shop.ItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public void save(String name, int cost, String description) {
        Item item = new Item(name, cost, description);
        itemRepository.save(item);
    }

    public void delete(long id) {
        Item item = itemRepository.findById(id).orElseThrow();
        itemRepository.delete(item);
    }

    public ArrayList<Item> findById(long id) {
        Optional<Item> item = itemRepository.findById(id);
        ArrayList<Item> items = new ArrayList<>();
        item.ifPresent(items::add);
        return items;
    }

    public void updateItem(long id, String name, int cost, String description) {
        Item item = itemRepository.findById(id).orElseThrow();
        if (!StringUtils.isEmpty(name)) {
            item.setName(name);
        }
        item.setCost(cost);
        item.setDescription(description);
        itemRepository.save(item);
    }
}