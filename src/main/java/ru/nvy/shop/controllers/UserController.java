package ru.nvy.shop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nvy.shop.models.user.Role;
import ru.nvy.shop.models.user.User;
import ru.nvy.shop.service.UserService;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * userList
     *
     * @param model
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userList(Model model) {
        model.addAttribute("title", "Users");
        model.addAttribute("users", userService.findAll());
        return "user/user";
    }


    @GetMapping("{user}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getUserRole(@PathVariable User user, Model model) {
        model.addAttribute("title", user);
        model.addAttribute("users", user);
        model.addAttribute("roles", Role.values());

        return "user/changeRole";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editUserRole(@PathVariable(value = "id") Long id, @RequestParam Map<String, String> form) {
        userService.roleChange(id, form);
        return "redirect:/blog";
    }

    /**
     * getProfile - to link specific user and edit page
     *
     * @param model
     * @param user
     * @return profile.html
     */
    @GetMapping("profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("title", user.getUsername());
        model.addAttribute("users", user);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("password", user.getPassword());
        model.addAttribute("email", user.getEmail());

        return "user/profile";
    }

    @PostMapping("profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String email
    ) {
        userService.updateProfile(user, username, password, email);

        return "redirect:/blog";
    }
}