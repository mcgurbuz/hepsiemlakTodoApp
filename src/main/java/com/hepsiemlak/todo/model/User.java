package com.hepsiemlak.todo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
@Data
public class User {
    @Id
    private String id;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private Set<Role> roles = new HashSet<>();
}
