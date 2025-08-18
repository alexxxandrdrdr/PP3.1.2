package com.example.demo.controller;


import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String admin(Model model) {
        model.addAttribute("usersList", userRepository.findAll());
        return "admin/panel";
    }

    @GetMapping("/add_user")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleRepository.findAll());
        return "admin/add_user";
    }

    @PostMapping("/add")
    public String createUser(@ModelAttribute("user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit_user/{id}")
    public String showEditForm(Model model, @PathVariable int id) {
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("user", userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь с id=" + id + " не найден")));
        return "admin/edit_user";
    }
    @PatchMapping("/edit_user/{id}")
    public String updateUser(@PathVariable int id,
                             @ModelAttribute("user") User formUser) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + id));

        // обновляем поля
        existingUser.setUsername(formUser.getUsername());

        // если пароль не пустой -> обновляем
        if (formUser.getPassword() != null && !formUser.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(formUser.getPassword()));
        }

        // обновляем роли
        if (formUser.getRoles() != null && !formUser.getRoles().isEmpty()) {
            // подтянуть роли по id из формы
            Set<Role> updatedRoles = formUser.getRoles().stream()
                    .map(role -> roleRepository.findById(role.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Роль не найдена: " + role.getId())))
                    .collect(Collectors.toSet());
            existingUser.setRoles(updatedRoles);
        } else {
            existingUser.getRoles().clear();
        }

        userRepository.save(existingUser);

        return "redirect:/admin";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userRepository.deleteById(id);
        return "redirect:/admin";
    }

}
