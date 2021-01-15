package com.example.demo.repositories;

import com.example.demo.Snippet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SnippetRepository extends CrudRepository<Snippet, Long> {
    Optional<Snippet> findById(Long id);

    Optional<Snippet> findAllByOwnerAccountId(Long id);

    List<Snippet> findAll();

    //@Query(value = "UPDATE snippet SET ") TODO:
}
