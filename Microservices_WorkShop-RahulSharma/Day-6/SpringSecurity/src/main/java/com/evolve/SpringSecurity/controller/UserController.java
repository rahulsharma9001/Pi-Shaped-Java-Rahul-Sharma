package com.evolve.SpringSecurity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/user")
    public String user(Authentication auth) {
        return "Hello User: " + auth.getName();
    }

    @GetMapping("/admin")
    public String admin(Authentication auth) {
        return "Hello Admin: " + auth.getName();
    }
}