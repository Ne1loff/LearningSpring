package com.example.demo;

import lombok.Data;


import javax.persistence.*;

@Data
@Entity
public class Snippet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User ownerId;

    private String name;
}
