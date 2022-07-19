package ru.nvy.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nvy.shop.models.Item;
import ru.nvy.shop.repos.ItemRepository;

@Controller
public class ShopController {
    @Autowired
    private final ItemRepository itemRepository;

    public ShopController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping("/shop")
    public String shop(Model model) {
        Iterable<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "shop";
    }

    @GetMapping("/shop/new")
    public String newItem(Model model) {
        return "new";
    }

    @PostMapping("/shop/new")
    public String create(@RequestParam String name, @RequestParam int cost,
                         @RequestParam String description, Model model) {
        Item item = new Item(name, cost, description);
        itemRepository.save(item);
        return "redirect:/shop";
    }

}
