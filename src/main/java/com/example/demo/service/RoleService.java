package com.example.demo.service;

import com.example.demo.model.Role;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface RoleService {

    List<Role> findAll();

    Role findById(Long id);

    static String rolesToString(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .sorted()
                .collect(Collectors.joining(", "));
    }
}
