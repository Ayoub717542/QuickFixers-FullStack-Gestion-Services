package com.example.QuickFixersBackend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequestDTO {
    @NotBlank
    @Email
    private String userEmail;
    @NotBlank
    @Size(min = 8)
    private String password;
}
