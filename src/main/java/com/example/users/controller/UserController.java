package com.example.users.controller;

import com.example.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        return ResponseEntity.ok().body(userService.getUser(username));
    }

    @GetMapping
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteUsersByIds(@RequestParam List<Long> ids) {
        userService.deleteUsers(ids);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/block/{id}")
    public ResponseEntity<?> blockUserById(@PathVariable Long id) {
        userService.blockUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/block")
    public ResponseEntity<?> blockUsersByIds(@RequestParam List<Long> ids) {
        userService.blockUsers(ids);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unblock/{id}")
    public ResponseEntity<?> unblockUserById(@PathVariable Long id) {
        userService.unblockUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unblock")
    public ResponseEntity<?> unblockUsersByIds(@RequestParam List<Long> ids) {
        userService.unblockUsers(ids);
        return ResponseEntity.ok().build();
    }
}
