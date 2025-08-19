package com.example.demo.service;



import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User findById(int id) throws IllegalArgumentException{
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь с id=" + id + " не найден"));
    }

    public void update(User formUser, int id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + id));

        existingUser.setUsername(formUser.getUsername());

        if (formUser.getPassword() != null && !formUser.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(formUser.getPassword()));
        }

        if (formUser.getRoles() != null && !formUser.getRoles().isEmpty()) {
            Set<Role> updatedRoles = formUser.getRoles().stream()
                    .map(role -> roleRepository.findById(role.getId())
                            .orElseThrow(() -> new IllegalArgumentException("Роль не найдена: " + role.getId())))
                    .collect(Collectors.toSet());
            existingUser.setRoles(updatedRoles);
        } else {
            existingUser.getRoles().clear();
        }
        userRepository.save(existingUser);
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }
}
