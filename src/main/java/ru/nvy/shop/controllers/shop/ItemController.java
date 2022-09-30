package ru.nvy.shop.controllers.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nvy.shop.controllers.ControllerUtils;
import ru.nvy.shop.models.blog.Message;
import ru.nvy.shop.models.shop.Item;
import ru.nvy.shop.service.shop.ItemService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

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
            @Valid Item item,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("item", item);
            return "shop/new";
        } else {
            itemService.save(item, file);
            model.addAttribute("item", null);
        }
        Iterable<Item> items = itemService.findAll();
        model.addAttribute("items", items);

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
    ) throws IOException {
        itemService.updateItem(id, name, cost, description);
        return "redirect:/shop";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable(value = "id") long id) {
        itemService.delete(id);
        return "redirect:/shop";
    }
}
