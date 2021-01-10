package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class SnippetController {

    private final List<Snippet> snippets = new ArrayList<>();
    private long counter = 0;

    @GetMapping("/snippets")
    public List<Snippet> snippets() {
        return snippets;
    }

    @GetMapping("/snippets/{id}")
    public Snippet snippet(@PathVariable("id") long id) {
        return snippets.get((int) --id);
    }

    @PutMapping("/put_snippet")
    public void snippet(@RequestBody Map<String, String> parameters) {
        Snippet snippet = new Snippet(++counter, parameters.get("name"));
        snippets.add(snippet);
    }
}
