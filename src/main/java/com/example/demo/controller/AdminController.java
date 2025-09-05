package com.example.demo.controller;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.UserEditDto;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.example.demo.service.RoleService.rolesToString;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private final Logger logger = LoggerFactory.getLogger(AdminController.class);

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String showAdminPanel(Model model, @AuthenticationPrincipal User user) {
        String roles = rolesToString(user.getRoles());
        model.addAttribute("authRoles", roles);
        model.addAttribute("authUser", user);
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("usersList", userService.findAll());
        return "admin/panel";
    }

    @GetMapping("/addUser")
    public String showCreateUser(@ModelAttribute("user") User user) {
        userService.save(user);
        return "admin/addNewUser";
    }

    @PostMapping("/addUser")
    public String createUser(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit-user/{id}")
    public String getUserForEdit(@PathVariable Long id, Model model) {
        try {
            UserEditDto userDto = new UserEditDto(userService.findById(id));
            if (userDto.getId() == null) {
                logger.warn("userDto is null");
                return "redirect:/admin";
            }
            List<Role> roles = roleService.findAll();
            model.addAttribute("userDto", userDto);
            model.addAttribute("rolesAll", roles);
            return "admin/editUser";
        } catch (Exception e) {
            return "redirect:/admin";
        }

    }

    @PatchMapping("/edit-user/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute UserEditDto userDto) {
        if (userDto.getId() != null && id != null) {
            userService.updateUser(userDto, id);
        } else throw new IllegalArgumentException("Пустой пользователь");
        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
