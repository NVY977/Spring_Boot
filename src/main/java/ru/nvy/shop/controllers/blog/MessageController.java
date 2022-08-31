package ru.nvy.shop.controllers.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.nvy.shop.models.user.User;
import ru.nvy.shop.models.blog.Message;
import ru.nvy.shop.repos.blog.MessageRepo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Controller
public class MessageController {
    @Autowired
    private final MessageRepo messageRepo;

    public MessageController(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    //ищем upload.path в файле настроек и вставляем в нашу uploadPath
    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/blog")
    public String blog(Model model) {
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);
        return "blog/blog";
    }

    @GetMapping("/blog/new")
    public String newMessage(Model model) {
        return "blog/new";
    }

    @PostMapping("/blog/new")
    public String create(@AuthenticationPrincipal User user, @RequestParam String text,
                         @RequestParam String tag, Model model, @RequestParam("file") MultipartFile file) throws IOException {
        Message message = new Message(text, tag, user);
        if (file != null) {
            File uploadDir = new File(uploadPath);
            // если не существует директории
            if (!uploadDir.exists()) {
                uploadDir.mkdir(); // создадим ее
            }
            // создаем уникальное имя файла, чтобы избавиться от коллизии
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));
            message.setFilename(resultFilename);
        }
        messageRepo.save(message);
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);
        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}")
    public String messageConcrete(@PathVariable(value = "id") long id, Model model) {
        if (!messageRepo.existsById(id)) {
            return "redirect:/blog";
        }
        Optional<Message> message = messageRepo.findById(id);
        ArrayList<Message> messages = new ArrayList<>();
        message.ifPresent(messages::add);
        model.addAttribute("messages", messages);
        return "blog/concrete";
    }

    @GetMapping("/blog/{id}/edit")
    public String messageEdit(@PathVariable(value = "id") long id, Model model) {
        if (!messageRepo.existsById(id)) {
            return "redirect:/blog";
        }
        Optional<Message> message = messageRepo.findById(id);
        ArrayList<Message> messages = new ArrayList<>();
        message.ifPresent(messages::add);
        model.addAttribute("messages", messages);
        return "blog/edit";
    }

    @PostMapping("/blog/{id}/edit")
    public String update(@PathVariable(value = "id") long id, @RequestParam String tag,
                         @RequestParam String text, Model model) {
        Message message = messageRepo.findById(id).orElseThrow();
        message.setTag(tag);
        message.setText(text);
        messageRepo.save(message);
        return "redirect:/blog";
    }

    @PostMapping("/blog/{id}/delete")
    public String delete(@PathVariable(value = "id") long id) {
        Message message = messageRepo.findById(id).orElseThrow();
        messageRepo.delete(message);
        return "redirect:/blog";
    }
}
