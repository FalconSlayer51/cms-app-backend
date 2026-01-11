package org.ramesh.backend.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.ramesh.backend.domain.enums.Role;

public record SignupRequest (
        @Email String email,
        @NotBlank String password,
        Role role
) {}
