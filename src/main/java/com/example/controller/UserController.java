package com.example.controller;


import com.example.domain.User;
import com.example.repository.UserRepository;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/login")
    public ResponseEntity<User> reg(@RequestBody User user) {
        if (!userRepository.existsByUsername(user.getUsername())) {
            User createdUser = new User(user.getUsername(), user.getPassword());
            userRepository.save(createdUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

//    @GetMapping("/login/{username}")
//    public ResponseEntity<User> registration(@PathVariable String username) {
//        if (!userRepository.existsByUsername(username)) {
//            User createdUser = new User(username);
//            userRepository.save(createdUser);
//            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }

//    @PostMapping("/login")
//    public ResponseEntity<User> reg(@RequestParam String username,
//                                    @RequestParam String password){
//        if (!userRepository.existsByUsername(username)) {
//            User createdUser = new User(username, password);
//            userRepository.save(createdUser);
//            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
//        }
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }
}
