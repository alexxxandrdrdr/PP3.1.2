package com.example.demo.model;


import com.example.demo.repository.RoleRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToRoleConverter implements Converter<String, Role> {
    private final RoleRepository roleRepository;

    public StringToRoleConverter(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role convert(String source) {
        Long id = Long.valueOf(source);
        return this.roleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Role not found: " + id));
    }
}
