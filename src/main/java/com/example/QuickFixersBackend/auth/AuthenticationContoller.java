package com.example.QuickFixersBackend.auth;

import com.example.QuickFixersBackend.dto.auth.AuthenticationRequestDTO;
import com.example.QuickFixersBackend.dto.auth.AuthenticationResponceDTO;
import com.example.QuickFixersBackend.dto.auth.RegisterRequoestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationContoller {

    private final  AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponceDTO> register(@RequestBody RegisterRequoestDTO register){
        return ResponseEntity.ok(authenticationService.register(register));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponceDTO> login(@RequestBody AuthenticationRequestDTO authenticationRequestDTO){
        return ResponseEntity.ok(authenticationService.login(authenticationRequestDTO));
    }
}
