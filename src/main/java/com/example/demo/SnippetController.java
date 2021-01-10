package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class SnippetController {

    private final Map<Long, Snippet> snippets = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(0);

    @GetMapping("/snippets")
    public List<Snippet> getSnippetsList() {
        return new ArrayList<>(snippets.values());
    }

    @GetMapping("/snippets/{id}")
    public Snippet getSnippet(@PathVariable("id") long id) {
        Snippet snippet = snippets.get(id);
        if (snippet == null)
            throw new NotFoundException();
        else return snippet;
    }

    @PutMapping("/snippets")
    public Snippet addSnippet(@RequestBody Snippet snippet) {
        long id = counter.incrementAndGet();
        snippet.setId(id);
        snippets.put(id, snippet);
        return snippet;
    }

    @PatchMapping("/snippets/{id}")
    public Snippet changeSnippet (@RequestBody Snippet newSnippet, @PathVariable("id") long id) {
        Snippet oldSnippet = snippets.get(id);
        if (oldSnippet == null)
            throw new NotFoundException();
        long mainId = oldSnippet.getId();
        oldSnippet = newSnippet;
        oldSnippet.setId(mainId);
        snippets.replace(mainId, oldSnippet);
        
        return oldSnippet;
    }
    
    @DeleteMapping("/snippets/{id}")
    public HttpStatus deleteSnippet (@PathVariable("id") long id) {
        Snippet snippet = snippets.get(id);
        if (snippet == null)
            throw new NotFoundException();
        else {
            snippets.remove(snippet.getId());
            return HttpStatus.OK;
        }
    }
}
