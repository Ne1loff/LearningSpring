package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
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
        Optional.ofNullable(snippet).orElseThrow(NotFoundException::new);
        return snippet;
    }

    @PutMapping("/snippets")
    public Snippet addSnippet(@RequestBody Snippet snippet) {
        long id = counter.incrementAndGet();
        snippet.setId(id);
        snippets.put(id, snippet);
        return snippet;
    }

    @PatchMapping("/snippets/{id}")
    public Snippet changeSnippet(@RequestBody Snippet newSnippet, @PathVariable("id") long id) {
        Snippet oldSnippet = snippets.get(id);
        Optional.ofNullable(oldSnippet).orElseThrow(NotFoundException::new);
        long mainId = oldSnippet.getId();
        oldSnippet = newSnippet;
        oldSnippet.setId(mainId);
        snippets.replace(mainId, oldSnippet);

        return oldSnippet;
    }

    @DeleteMapping("/snippets/{id}")
    public HttpStatus deleteSnippet(@PathVariable("id") long id) {
        Snippet snippet = snippets.get(id);
        Optional.ofNullable(snippet).orElseThrow(NotFoundException::new);

        snippets.remove(snippet.getId());
        return HttpStatus.OK;
    }

    @GetMapping("/snippets/{snippetId}/{codeFileName}")
    public CodeFile getCodeFile(@PathVariable("snippetId") long id, @PathVariable("codeFileName") String fileName) {
        Snippet snippet = snippets.get(id);
        Optional.ofNullable(snippet).orElseThrow(NotFoundException::new);

        return snippet.getContent().stream()
                .filter(it -> it.getName().equals(fileName))
                .findFirst()
                .orElseThrow(NotFoundException::new);
    }

    @PutMapping("/snippets/{snippetId}/{codeFileName}")
    public HttpStatus putCodeFile(
            @RequestBody CodeFile newCodeFile,
            @PathVariable("snippetId") long id,
            @PathVariable("codeFileName") String fileName
    ) {
        Snippet snippet = snippets.get(id);
        Optional.ofNullable(snippet).orElseThrow(NotFoundException::new);

        CopyOnWriteArrayList<CodeFile> newContent = new CopyOnWriteArrayList<>();
        newCodeFile.setName(fileName);
        newContent.add(newCodeFile);
        snippet.setContent(newContent);
        return HttpStatus.OK;
    }

    @PatchMapping("/snippets/{snippetId}/{codeFileName}")
    public CodeFile changeCodeFile(
            @RequestBody CodeFile newCodeFile,
            @PathVariable("snippetId") long id,
            @PathVariable("codeFileName") String fileName
    ) {
        Snippet snippet = snippets.get(id);
        Optional.ofNullable(snippet).orElseThrow(NotFoundException::new);

        CopyOnWriteArrayList<CodeFile> changedContent = snippet.getContent();
        newCodeFile.setName(fileName);
        changedContent.replaceAll(it -> it.getName().equals(fileName) ? newCodeFile : it);
        snippet.setContent(changedContent);
        return newCodeFile;
    }

    @DeleteMapping("/snippets/{snippetId}/{codeFileName}")
    public HttpStatus deleteCodeFile(
            @PathVariable("snippetId") long id,
            @PathVariable("codeFileName") String fileName
    ) {
        Snippet snippet = snippets.get(id);
        Optional.ofNullable(snippet).orElseThrow(NotFoundException::new);

        CopyOnWriteArrayList<CodeFile> changedContent = snippet.getContent();

        CodeFile removedCodeFile = changedContent.stream()
                .filter(it -> it.getName().equals(fileName))
                .findFirst()
                .orElseThrow(NotFoundException::new);

        if (!changedContent.remove(removedCodeFile))
            throw new NotFoundException();

        snippet.setContent(changedContent);
        return HttpStatus.OK;
    }
}
