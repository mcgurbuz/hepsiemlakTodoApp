package com.hepsiemlak.todo.controller;

import com.hepsiemlak.todo.dto.request.LoginRequest;
import com.hepsiemlak.todo.dto.request.RegisterRequest;
import com.hepsiemlak.todo.dto.response.LoginResponse;
import com.hepsiemlak.todo.dto.response.ResponseMessage;
import com.hepsiemlak.todo.dto.response.SuccessResponse;
import com.hepsiemlak.todo.model.User;
import com.hepsiemlak.todo.security.jwt.JWTTokenProvider;
import com.hepsiemlak.todo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping
public class UserJWTController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse> register(
            @Valid @RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest);

        SuccessResponse response = new SuccessResponse();
        response.setMessage(ResponseMessage.REGISTER_RESPONSE_MESSAGE);
        response.setSuccess(true);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken authToken = new
                UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        Authentication auth = authenticationManager.authenticate(authToken);
        String jwtToken = jwtTokenProvider.generateJTWToken(auth);

        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = userService.getOneUserByUsername(loginRequest.getUsername());


        LoginResponse response = new LoginResponse();
        response.setAccessToken(jwtToken);
        response.setUserId(user.getId());
        response.setMessage(user.getUsername() + " take your token");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
