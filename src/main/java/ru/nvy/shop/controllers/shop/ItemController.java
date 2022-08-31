package ru.nvy.shop.controllers.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nvy.shop.models.shop.Item;
import ru.nvy.shop.repos.shop.ItemRepository;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping("/shop")
public class ItemController {
    @Autowired
    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping()
    public String shop(Model model) {
        Iterable<Item> items = itemRepository.findAll();
        model.addAttribute("title", "Items");
        model.addAttribute("items", items);
        return "shop/shop";
    }

    @GetMapping("/new")
    public String newItem(Model model) {
        model.addAttribute("title", "New Item");
        return "shop/new";
    }

    @PostMapping("/new")
    public String create(@RequestParam String name, @RequestParam int cost,
                         @RequestParam String description, Model model) {
        Item item = new Item(name, cost, description);
        itemRepository.save(item);
        return "redirect:/shop";
    }

    @GetMapping("/{id}")
    public String itemConcrete(@PathVariable(value = "id") long id, Model model) {
        if (!itemRepository.existsById(id)) {
            return "redirect:/shop";
        }
        Optional<Item> item = itemRepository.findById(id);
        ArrayList<Item> items = new ArrayList<>();
        item.ifPresent(items::add);
        model.addAttribute("title", "Concrete Item");
        model.addAttribute("items", items);
        return "shop/concrete";
    }

    @GetMapping("/{id}/edit")
    public String itemEdit(@PathVariable(value = "id") long id, Model model) {
        if (!itemRepository.existsById(id)) {
            return "redirect:/shop";
        }
        Optional<Item> item = itemRepository.findById(id);
        ArrayList<Item> items = new ArrayList<>();
        item.ifPresent(items::add);
        model.addAttribute("title", "Edit item");
        model.addAttribute("items", items);
        return "shop/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable(value = "id") long id, @RequestParam String name, @RequestParam int cost,
                         @RequestParam String description, Model model) {
        Item item = itemRepository.findById(id).orElseThrow();
        item.setName(name);
        item.setCost(cost);
        item.setDescription(description);
        itemRepository.save(item);
        return "redirect:/shop";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable(value = "id") long id) {
        Item item = itemRepository.findById(id).orElseThrow();
        itemRepository.delete(item);
        return "redirect:/shop";
    }
}
