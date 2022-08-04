package com.example.users.controller;

import com.example.users.entity.UserEntity;
import com.example.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/login")
    public ResponseEntity<?> login(Principal principal) {
        UserEntity user = userService.getUser(principal.getName());
        if (user != null) {
            user.setLastLogin(LocalDateTime.now());
            userService.saveUser(user);
            return ResponseEntity.ok().body(user);
        } else {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }
}
