package com.example.QuickFixersBackend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RegisterRequoestDTO {
    private String nom;
    private String prenom;
    private String email;
    private String password;
}

