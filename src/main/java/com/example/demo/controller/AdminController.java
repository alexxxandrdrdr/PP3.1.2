package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.model.UserEditDto;
import com.example.demo.service.RoleServiceImpl;
import com.example.demo.service.UserServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.service.RoleService.rolesToString;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserServiceImpl userServiceImpl;
    private final RoleServiceImpl roleServiceImpl;

    public AdminController(UserServiceImpl userServiceImpl, RoleServiceImpl roleServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.roleServiceImpl = roleServiceImpl;
    }

    @GetMapping
    public String showAdminPanel(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("authRoles", rolesToString(user.getRoles()));
        model.addAttribute("authUser", user);
        model.addAttribute("roles", roleServiceImpl.findAll());
        model.addAttribute("usersList", userServiceImpl.findAll());
        return "admin/panel";
    }

    @GetMapping("/addUser")
    public String showCreateUser(@ModelAttribute("user") User user) {
        userServiceImpl.save(user);
        return "admin/addNewUser";
    }

    @PostMapping("/addUser")
    public String createUser(@ModelAttribute("user") User user) {
        userServiceImpl.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit-user/{id}")
    public String getUserForEdit(@PathVariable Long id, Model model) {
        try {
            UserEditDto userDto = new UserEditDto(userServiceImpl.findById(id));
            model.addAttribute("userDto", userDto);
            model.addAttribute("rolesAll", roleServiceImpl.findAll());
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", "Пользователь с id:" + id + "не найден");
        }
        return "admin/editUser";
    }

    @PatchMapping("/edit-user/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute UserEditDto userDto) {
        userServiceImpl.updateUser(userDto, id);
        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userServiceImpl.deleteUser(id);
        return "redirect:/admin";
    }
}
