package ru.nvy.shop.controllers.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nvy.shop.models.shop.Item;
import ru.nvy.shop.repos.shop.ItemRepository;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class ItemController {
    @Autowired
    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping("/shop")
    public String shop(Model model) {
        Iterable<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "shop/shop";
    }

    @GetMapping("/shop/new")
    public String newItem(Model model) {
        return "shop/new";
    }

    @PostMapping("/shop/new")
    public String create(@RequestParam String name, @RequestParam int cost,
                         @RequestParam String description, Model model) {
        Item item = new Item(name, cost, description);
        itemRepository.save(item);
        return "redirect:/shop";
    }

    @GetMapping("/shop/{id}")
    public String itemConcrete(@PathVariable(value = "id") long id, Model model) {
        if (!itemRepository.existsById(id)) {
            return "redirect:/shop";
        }
        Optional<Item> item = itemRepository.findById(id);
        ArrayList<Item> items = new ArrayList<>();
        item.ifPresent(items::add);
        model.addAttribute("items", items);
        return "shop/concrete";
    }

    @GetMapping("/shop/{id}/edit")
    public String itemEdit(@PathVariable(value = "id") long id, Model model) {
        if (!itemRepository.existsById(id)) {
            return "redirect:/shop";
        }
        Optional<Item> item = itemRepository.findById(id);
        ArrayList<Item> items = new ArrayList<>();
        item.ifPresent(items::add);
        model.addAttribute("items", items);
        return "shop/edit";
    }

    @PostMapping("/shop/{id}/edit")
    public String update(@PathVariable(value = "id") long id, @RequestParam String name, @RequestParam int cost,
                         @RequestParam String description, Model model) {
        Item item = itemRepository.findById(id).orElseThrow();
        item.setName(name);
        item.setCost(cost);
        item.setDescription(description);
        itemRepository.save(item);
        return "redirect:/shop";
    }

    @PostMapping("/shop/{id}/delete")
    public String delete(@PathVariable(value = "id") long id) {
        Item item = itemRepository.findById(id).orElseThrow();
        itemRepository.delete(item);
        return "redirect:/shop";
    }
}
