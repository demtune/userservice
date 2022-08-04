package com.example.users.controller;

import com.example.users.dto.RegistrationUserDto;
import com.example.users.entity.UserEntity;
import com.example.users.exception.UserAlreadyExistException;
import com.example.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RegisterController {

    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationUserDto userDto) {
        try {
            Optional<UserEntity> user = userService.registration(userDto);
            return ResponseEntity.ok().body(user);
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка в добавлении пользователя");
        }
    }
}
