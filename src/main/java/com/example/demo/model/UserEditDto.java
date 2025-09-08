package com.example.demo.model;

import java.util.List;
import java.util.stream.Collectors;

public class UserEditDto {
    private Long id;
    private String username;
    private String password;
    private List<Long> roleIds;

    public UserEditDto() {
    }

    public UserEditDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.roleIds = user.getRoles().stream().map(Role::getId).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoles(List<Long> roles) {
        this.roleIds = roles;
    }
}
