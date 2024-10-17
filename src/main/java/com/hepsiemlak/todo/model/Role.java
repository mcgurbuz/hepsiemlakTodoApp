package com.hepsiemlak.todo.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Document
@Data
public class Role {
    @Id
    private String id;
    private String roleName;
    private String description;
}
