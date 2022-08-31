package ru.nvy.shop.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nvy.shop.models.shop.Item;
import ru.nvy.shop.models.user.Role;
import ru.nvy.shop.models.user.User;
import ru.nvy.shop.repos.user.UserRepo;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')") // перед выполнением любого метода проверяем есть ли у него права
public class UserController {

    @Value("${upload.path}")
    private String uploadPath;
    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("title", "Users");
        model.addAttribute("users", userRepo.findAll());

        return "user/user";
    }

    @GetMapping("{user}")
    public String userEdit(@PathVariable User user, Model model) {
        model.addAttribute("title", user);
        model.addAttribute("users", user);
        model.addAttribute("roles", Role.values());

        return "user/edit";
    }

    @PostMapping("/{id}")
    public String userSave(@PathVariable(value = "id") long id,
                           @RequestParam String username, @RequestParam Map<String, String> form){
        User user = userRepo.findById(id).orElseThrow();
        user.setUsername(username);
        //список ролей которые уже установлены
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        // очищение ролей (каждый раз новую роль присваиваем)
        user.getRoles().clear();
        // устанавливаем новые роли в соответствии с формой(интерфейсом) который отправили
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepo.save(user);

        return "redirect:/user";
    }
}