package com.example.demo;

import lombok.Data;

import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class Snippet {
    private long id;
    private long ownerId;
    private String name;
    private CopyOnWriteArrayList<CodeFile> content;
}
