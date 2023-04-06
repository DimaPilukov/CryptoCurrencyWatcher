package com.example.controller;

import com.example.domain.User;
import org.springframework.http.HttpStatus;
import com.example.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/registration")
    public ResponseEntity<User> registration(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
            User createdUser = new User(user.getUsername(), user.getPassword());
            userRepository.save(createdUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}