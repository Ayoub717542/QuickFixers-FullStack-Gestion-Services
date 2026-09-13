package com.example.QuickFixersBackend.dto.support;

import com.example.QuickFixersBackend.enums.ServiceType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateSupportRequestDTO {
    @NotBlank private String nom;
    @NotBlank private String prenom;
    @NotBlank
    @Email
    private String email;
    @NotBlank private String password;
    @NotNull
    private ServiceType serviceType;
}
