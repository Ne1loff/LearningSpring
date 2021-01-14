package com.example.demo;

import com.example.demo.repositories.CodeFileRepository;
import com.example.demo.repositories.SnippetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class SnippetController {

    private final SnippetRepository snippetRepository;

    private final CodeFileRepository codeFileRepository;

    public SnippetController(SnippetRepository snippetRepository, CodeFileRepository codeFileRepository) {
        this.snippetRepository = snippetRepository;
        this.codeFileRepository = codeFileRepository;
    }

    @GetMapping("/snippets")
    public List<Snippet> getSnippetsList() {
        return snippetRepository.findAll();
    }

    @GetMapping("/snippets/{id}")
    public Snippet getSnippet(@PathVariable("id") long id) {
        return snippetRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @PutMapping("/snippets")
    public Snippet addSnippet(@RequestBody Snippet snippet) {
        snippetRepository.save(snippet);
        return snippet;
    }

    @PatchMapping("/snippets/{id}")
    public Snippet changeSnippet(@RequestBody Snippet newSnippet, @PathVariable("id") long id) {

        return snippetRepository.findById(id).map(snippet -> {
            snippet.setName(newSnippet.getName());
            return snippetRepository.save(snippet);
        }).orElseThrow(NotFoundException::new);
    }

    @DeleteMapping("/snippets/{id}")
    public HttpStatus deleteSnippet(@PathVariable("id") long id) {
        snippetRepository.deleteById(id);
        return snippetRepository.findById(id).equals(Optional.empty()) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
    }

    //CodeFile

    @GetMapping("/snippets/{snippetId}/{codeFileName}")
    public CodeFile getCodeFile(@PathVariable("snippetId") long id, @PathVariable("codeFileName") String fileName) {
        return codeFileRepository.findBySnippetIdAndName(id, fileName).orElseThrow(NotFoundException::new);
    }

    @PutMapping("/snippets/{snippetId}/{codeFileName}")
    public HttpStatus putCodeFile(
            @RequestBody CodeFile newCodeFile,
            @PathVariable("snippetId") long id,
            @PathVariable("codeFileName") String fileName
    ) {
        snippetRepository.findById(id).map(snippet -> {
            newCodeFile.setName(fileName);
            return codeFileRepository.save(newCodeFile);
        }).orElseThrow(NotFoundException::new);
        return HttpStatus.OK;
    }

    @PatchMapping("/snippets/{snippetId}/{codeFileName}")
    public CodeFile changeCodeFile(
            @RequestBody CodeFile newCodeFile,
            @PathVariable("snippetId") long id,
            @PathVariable("codeFileName") String fileName
    ) {
        return codeFileRepository.findBySnippetIdAndName(id, fileName)
                .map(file -> {
                    file.setName(newCodeFile.getName());
                    return codeFileRepository.save(file);
                }).orElseThrow(NotFoundException::new);
    }

    @DeleteMapping("/snippets/{snippetId}/{codeFileName}")
    public HttpStatus deleteCodeFile(
            @PathVariable("snippetId") long id,
            @PathVariable("codeFileName") String fileName
    ) {

        return codeFileRepository.deleteBySnippetIdAndName(id, fileName).equals(Optional.empty()) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
    }
}
