package ru.nvy.shop.controllers.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nvy.shop.service.shop.ItemService;

@Controller
@RequestMapping("/shop")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping()
    public String shop(Model model) {
        model.addAttribute("title", "Items");
        model.addAttribute("items", itemService.findAll());
        return "shop/shop";
    }

    @GetMapping("/new")
    public String newItem(Model model) {
        model.addAttribute("title", "New Item");
        return "shop/new";
    }

    @PostMapping("/new")
    public String create(
            @RequestParam String name,
            @RequestParam int cost,
            @RequestParam String description
    ) {
        itemService.save(name, cost, description);
        return "redirect:/shop";
    }

    @GetMapping("/{id}")
    public String itemConcrete(@PathVariable(value = "id") long id, Model model) {
        model.addAttribute("title", "Concrete Item");
        model.addAttribute("items", itemService.findById(id));
        return "shop/concrete";
    }

    @GetMapping("/{id}/edit")
    public String itemEdit(@PathVariable(value = "id") long id, Model model) {
        model.addAttribute("title", "Edit item");
        model.addAttribute("items", itemService.findById(id));
        return "shop/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable(value = "id") long id,
            @RequestParam String name,
            @RequestParam int cost,
            @RequestParam String description
    ) {
        itemService.updateItem(id, name, cost, description);
        return "redirect:/shop";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable(value = "id") long id) {
        itemService.delete(id);
        return "redirect:/shop";
    }
}
