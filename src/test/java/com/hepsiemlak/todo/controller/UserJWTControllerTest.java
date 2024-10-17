package com.hepsiemlak.todo.controller;

import com.couchbase.client.core.deps.com.fasterxml.jackson.databind.ObjectMapper;
import com.hepsiemlak.todo.dto.request.LoginRequest;
import com.hepsiemlak.todo.dto.request.RegisterRequest;
import com.hepsiemlak.todo.dto.response.ResponseMessage;
import com.hepsiemlak.todo.model.User;
import com.hepsiemlak.todo.security.jwt.JWTTokenProvider;
import com.hepsiemlak.todo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
public class UserJWTControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserJWTController userJWTController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userJWTController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void register() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setUsername("testUser");
        registerRequest.setPassword("password");

        doNothing().when(userService).register(any(RegisterRequest.class));

        // MockMvc ile POST isteği gönder ve doğrula
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(ResponseMessage.REGISTER_RESPONSE_MESSAGE))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void login() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("password");

        // Kullanıcı bilgileri ve JWT oluşturma ayarları
        User user = new User();
        user.setId("1");
        user.setUsername("testUser");

        // Mock davranışı ayarlama
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(jwtTokenProvider.generateJTWToken(any(Authentication.class))).thenReturn("mockJwtToken");
        when(userService.getOneUserByUsername("testUser")).thenReturn(user);

        // MockMvc ile POST isteği gönder ve doğrula
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").value("mockJwtToken"))
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.message").value("testUser take your token"));
    }
}
