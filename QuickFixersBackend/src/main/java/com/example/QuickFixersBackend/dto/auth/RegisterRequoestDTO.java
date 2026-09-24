package com.example.QuickFixersBackend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RegisterRequoestDTO {
    @NotBlank
    private String nom;
    @NotBlank private String prenom;
    @NotBlank @Email
    private String email;
    @NotBlank
    @Size(min = 8)
    private String password;
}

