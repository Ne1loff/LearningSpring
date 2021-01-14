package com.example.demo;

import com.example.demo.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> getUserList() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable long id) {
        return userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    @PutMapping("/users")
    public User addUser(@RequestBody User user) {
        userRepository.save(user);
        return user;
    }

    @DeleteMapping("/users/{id}")
    public HttpStatus deleteUser(@PathVariable long id) {
        userRepository.deleteById(id);
        return userRepository.findById(id)
                .equals(Optional.empty()) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
    }
}
