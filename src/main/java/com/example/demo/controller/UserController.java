package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/user"})
public class UserController {
    public UserController() {
    }

    @GetMapping
    public String userpage() {
        return "user/user";
    }
}

