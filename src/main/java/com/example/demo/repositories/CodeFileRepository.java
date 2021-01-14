package com.example.demo.repositories;

import com.example.demo.CodeFile;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CodeFileRepository extends CrudRepository<CodeFile, Long> {
    Optional<CodeFile> findAllById(Long id);

    Optional<CodeFile> findBySnippetIdAndName(Long id, String name);

    Optional<CodeFile> deleteBySnippetIdAndName(Long id, String name);


}
