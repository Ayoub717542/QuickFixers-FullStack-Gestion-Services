package com.example.QuickFixersBackend.dto.user;

import com.example.QuickFixersBackend.enums.ServiceType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserResponseDTO implements Serializable {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String role;
    private ServiceType serviceType;

}
