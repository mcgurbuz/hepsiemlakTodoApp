package com.hepsiemlak.todo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotNull(message="Please provide a user name")
    private String username;

    @NotNull(message="Please provide a password")
    private String password;
}