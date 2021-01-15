package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "account")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Snippet> snippets;


    private String location;
    private String passwordHash;

    @JsonProperty
    public void setLocation(String location) {
        this.location = location;
    }

    @JsonProperty
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @JsonIgnore
    public String getLocation() {
        return location;
    }

    @JsonIgnore
    public String getPasswordHash() {
        return passwordHash;
    }
}
