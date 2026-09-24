package com.example.QuickFixersBackend.dto.user;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserEditRequestDTO{
    @NotBlank private String nom;
    @NotBlank private String prenom;
    @NotBlank @Email private String email;
}
