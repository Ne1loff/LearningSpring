package com.example.demo;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Snippet {

    private long id;
    private long ownerId;
    private String name;
    private ArrayList<CodeFile> content;
}
