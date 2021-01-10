package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class SnippetController {

    private final List<Snippet> snippets = new CopyOnWriteArrayList<>();
    private final AtomicLong counter = new AtomicLong(0);

    @GetMapping("/snippets")
    public List<Snippet> getSnippetsList() {
        return snippets;
    }

    @GetMapping("/snippets/{id}")
    public Snippet getSnippet(@PathVariable("id") long id) {
        return snippets.stream()
                .filter(it -> it.getId() == id)
                .findFirst()
                .orElseThrow(SnippetNotFoundException::new);
    }

    @PutMapping("/snippets")
    public synchronized Snippet addSnippet(@RequestBody Snippet snippet) {
        snippet.setId(counter.incrementAndGet());
        snippets.add(snippet);
        return snippet;
    }
}
