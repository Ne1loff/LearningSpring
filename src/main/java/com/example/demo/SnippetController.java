package com.example.demo;

import com.example.demo.repositories.CodeFileRepository;
import com.example.demo.repositories.SnippetRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class SnippetController {

    private final SnippetRepository snippetRepository;
    private final CodeFileRepository codeFileRepository;
    private final UserRepository userRepository;

    public SnippetController(SnippetRepository snippetRepository, CodeFileRepository codeFileRepository, UserRepository userRepository) {
        this.snippetRepository = snippetRepository;
        this.codeFileRepository = codeFileRepository;
        this.userRepository = userRepository;
    }


    @GetMapping("/snippets")
    public List<Snippet> getSnippetsList() {
        return snippetRepository.findAll();
    }

    @GetMapping("/snippets/{ownerId}")
    public Snippet getSnippet(@PathVariable("ownerId") long id) {
        return snippetRepository.findAllByOwnerAccountId(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    @PutMapping("/snippets")
    public Snippet addSnippet(@RequestBody Snippet snippet) {
        User user = userRepository.findById(1L).orElseThrow(NotFoundException::new);
        user.getSnippets().add(snippet);
        return snippet;
    }

    @Transactional
    @PatchMapping("/snippets/{id}")
    public Snippet changeSnippet(@RequestBody Snippet newSnippet, @PathVariable("id") long id) {

        Snippet snippet = snippetRepository.findById(id).orElseThrow(NotFoundException::new);
        snippet.setName(newSnippet.getName());

        return snippet;
    }

    @Transactional
    @DeleteMapping("/snippets/{id}")
    public HttpStatus deleteSnippet(@PathVariable("id") long id) {
        snippetRepository.deleteById(id);
        return snippetRepository.findById(id).equals(Optional.empty()) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
    }

    //CodeFile

    @GetMapping("/snippets/{snippetId}/{codeFileId}")
    public CodeFile getCodeFile(@PathVariable("snippetId") long snippetId, @PathVariable("codeFileId") long codeFileId) {
        Snippet snippet = snippetRepository.findById(snippetId).orElseThrow(NotFoundException::new);
        if(snippet.getContent().containsKey(codeFileId))
            return snippet.getContent().remove(codeFileId);
        else
            throw new NotFoundException();
    }

    @Transactional
    @PutMapping("/snippets/{snippetId}/")
    public HttpStatus putCodeFile(
            @RequestBody CodeFile newCodeFile,
            @PathVariable("snippetId") long id
    ) {
        Map<Long, CodeFile> idCodeFileMap = snippetRepository.findById(id)
                .map(Snippet::getContent)
                .orElseThrow(NotFoundException::new);

        codeFileRepository.save(newCodeFile);
        idCodeFileMap.put(newCodeFile.getId(), newCodeFile);

        return HttpStatus.OK;
    }

    @Transactional
    @PatchMapping("/snippets/{snippetId}/{codeFileId}")
    public CodeFile changeCodeFile(
            @RequestBody CodeFile newCodeFile,
            @PathVariable("snippetId") long snippetId,
            @PathVariable("codeFileId") long codeFileId
    ) {
        Snippet snippet = snippetRepository.findById(snippetId).orElseThrow(NotFoundException::new);

        Map<Long, CodeFile> codeFiles = snippet.getContent();

        if (codeFiles.containsKey(codeFileId)) {
            CodeFile codeFile = codeFiles.get(codeFileId);
            codeFile.setName(newCodeFile.getName());
            codeFile.setText(newCodeFile.getText());
            return codeFile;
        } else {
            throw new NotFoundException();
        }
    }

    @Transactional
    @DeleteMapping("/snippets/{snippetId}/{codeFileName}")
    public HttpStatus deleteCodeFile(
            @PathVariable("snippetId") long id,
            @PathVariable("codeFileName") Long codeFileId
    ) {
        Snippet snippet = snippetRepository.findById(id).orElseThrow(NotFoundException::new);
        Map<Long, CodeFile> content = snippet.getContent();

        return content.remove(codeFileId) == null
                 ? HttpStatus.NOT_FOUND : HttpStatus.OK;
    }
}
