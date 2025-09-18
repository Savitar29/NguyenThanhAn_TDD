package com.example.springtest.controller;

import com.example.springtest.dto.LoginRequest;
import com.example.springtest.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_return_access_token_after_successfully_login() throws Exception {
        String email = "xuanthangsn@gmail.com";
        String password = "password";
        LoginRequest loginRequest = LoginRequest.builder().email(email).password(password).build();

        // setup mock
        when(userService.login(loginRequest.getEmail(), loginRequest.getPassword())).thenReturn("access_token");

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        ).andExpect(
            status().isOk()
        );

    }


    @Test
    void should_return_401_error_if_email_or_password_is_invalid() throws Exception {
        String email = "xuanthangsn@gmail.com";
        String password = "password";
        LoginRequest loginRequest = LoginRequest.builder().email(email).password(password).build();

        // setup mock
        when(userService.login(loginRequest.getEmail(), loginRequest.getPassword())).thenThrow(new IllegalArgumentException("Invalid email"));

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        ).andExpect(
                status().isBadRequest()
        );

    }


}
