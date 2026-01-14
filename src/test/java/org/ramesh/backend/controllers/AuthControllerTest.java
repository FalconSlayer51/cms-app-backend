package org.ramesh.backend.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.ramesh.backend.domain.dto.AuthResponse;
import org.ramesh.backend.domain.dto.LoginRequest;
import org.ramesh.backend.domain.dto.SignupRequest;
import org.ramesh.backend.domain.entities.User;
import org.ramesh.backend.domain.enums.Role;
import org.ramesh.backend.repository.UserRepository;
import org.ramesh.backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Login returns token on success")
    void loginSuccess() throws Exception {
        String email = "test@example.com";
        String password = "password";
        String token = "jwt-token";
        User user = new User(UUID.randomUUID(), email, "hashed", Role.ADMIN, OffsetDateTime.now());
        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(user));
        Mockito.when(jwtService.generateToken(eq(email), any())).thenReturn(token);
        String body = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    @DisplayName("Login returns 404 if user not found")
    void loginUserNotFound() throws Exception {
        String email = "notfound@example.com";
        String password = "password";
        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());
        String body = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Signup returns 409 if user exists")
    void signupConflict() throws Exception {
        String email = "exists@example.com";
        SignupRequest req = new SignupRequest(email, "password", Role.ADMIN);
        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(new User()));
        String body = "{\"email\":\"" + email + "\",\"password\":\"password\",\"role\":\"ADMIN\"}";
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Signup returns 201 on success")
    void signupSuccess() throws Exception {
        String email = "new@example.com";
        SignupRequest req = new SignupRequest(email, "password", Role.ADMIN);
        Mockito.when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(any())).thenReturn("hashed");
        String body = "{\"email\":\"" + email + "\",\"password\":\"password\",\"role\":\"ADMIN\"}";
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated());
    }
}

