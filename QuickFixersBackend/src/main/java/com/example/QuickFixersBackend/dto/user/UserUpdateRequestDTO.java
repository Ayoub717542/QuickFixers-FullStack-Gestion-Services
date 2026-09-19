package com.example.QuickFixersBackend.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDTO {
    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;
}