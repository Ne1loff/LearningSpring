package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class UserController {

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(0);

    @GetMapping("/users")
    public List<User> getSnippetsList() {
        return new ArrayList<>(users.values());
    }

    @GetMapping("/users/{id}")
    public User getSnippet(@PathVariable long id) {
        User user = users.get(id);
        if (user == null)
            throw new NotFoundException();
        else return user;
    }

    @PutMapping("/users")
    public User addUser(@RequestBody User user) {
        long id = counter.incrementAndGet();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @DeleteMapping("/users/{id}")
    public HttpStatus deleteUser(@PathVariable long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException();
        } else {
            users.remove(id);
            return HttpStatus.OK;
        }
    }
}
