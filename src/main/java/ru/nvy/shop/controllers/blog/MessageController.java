package ru.nvy.shop.controllers.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nvy.shop.models.blog.Message;
import ru.nvy.shop.models.user.User;
import ru.nvy.shop.repos.blog.MessageRepo;
import ru.nvy.shop.service.UserService;
import ru.nvy.shop.service.blog.MessageService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/blog")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String blog(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("users", user);
        model.addAttribute("title", "Blog");
        model.addAttribute("messages", messageService.findAll());
        return "blog/blog";
    }

    @GetMapping("/new")
    public String newMessage(Model model) {
        model.addAttribute("title", "New post");
        return "blog/new";
    }

    @PostMapping("/new")
    public String create(@AuthenticationPrincipal User user,
                         @RequestParam String text,
                         @RequestParam String tag,
                         Model model,
                         @RequestParam("file") MultipartFile file
    ) throws IOException {
        model.addAttribute("messages", messageService.save(text, tag, user, file));
        return "redirect:/blog";
    }

    @GetMapping("/{id}")
    public String messageConcrete(@PathVariable(value = "id") long id, Model model) {
        model.addAttribute("title", "Concrete Message");
        model.addAttribute("messages", messageService.findById(id));
        return "blog/concrete";
    }

    @GetMapping("/{id}/edit")
    public String messageEdit(@PathVariable(value = "id") long id, Model model) {
        model.addAttribute("title", "Edit Message");
        model.addAttribute("messages", messageService.findById(id));
        return "blog/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable(value = "id") long id,
            @RequestParam String tag,
            @RequestParam String text,
            Model model
    ) {
        messageService.updateItem(id, text, tag);
        return "redirect:/blog";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable(value = "id") long id) {
        messageService.delete(id);
        return "redirect:/blog";
    }
}
