package com.hepsiemlak.todo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

  private String message;
  private String userId;
  private String accessToken;

}