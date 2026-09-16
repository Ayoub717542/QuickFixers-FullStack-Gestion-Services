package com.example.QuickFixersBackend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponceDTO {
    private String token;
    private String userEmail;
}
