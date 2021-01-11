package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class User {
    private long id;
    private String name;
    private String email;

    @JsonIgnore
    private String location;

    @JsonIgnore
    private String passwordHash;
}
