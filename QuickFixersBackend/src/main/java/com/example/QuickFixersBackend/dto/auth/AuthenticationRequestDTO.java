package com.example.QuickFixersBackend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequestDTO {
    @NotBlank
    @Email
    private String userEmail;
    @NonNull
    private String password;
}
