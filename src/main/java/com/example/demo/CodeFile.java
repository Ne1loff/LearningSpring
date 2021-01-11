package com.example.demo;

import lombok.Data;

@Data
public class CodeFile {
    private String name;
    private long modified;
    private long created;
}
