package ru.nvy.shop.controllers.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nvy.shop.controllers.ControllerUtils;
import ru.nvy.shop.models.blog.Message;
import ru.nvy.shop.models.user.User;
import ru.nvy.shop.service.UserService;
import ru.nvy.shop.service.blog.MessageService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

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
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
            return "blog/new";
        } else {
            messageService.save(message, user, file);
            model.addAttribute("message", null);
        }
        Iterable<Message> messages = messageService.findAll();
        model.addAttribute("messages", messages);
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
