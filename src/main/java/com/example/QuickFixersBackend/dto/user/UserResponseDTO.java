package com.example.QuickFixersBackend.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String role;
}
