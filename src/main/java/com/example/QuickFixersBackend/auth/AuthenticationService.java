package com.example.QuickFixersBackend.auth;

import com.example.QuickFixersBackend.dto.auth.AuthenticationRequestDTO;
import com.example.QuickFixersBackend.dto.auth.AuthenticationResponceDTO;
import com.example.QuickFixersBackend.dto.auth.RegisterRequoestDTO;
import com.example.QuickFixersBackend.enums.Role;
import com.example.QuickFixersBackend.entity.User;
import com.example.QuickFixersBackend.repository.UserRepository;
import com.example.QuickFixersBackend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private  final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;
    private  final JwtService jwtService;

    private  final AuthenticationManager authenticationManager;

    public @Nullable AuthenticationResponceDTO register(RegisterRequoestDTO register) {
        User user = User.builder()
                .nom(register.getNom())
                .prenom(register.getPrenom())
                .email(register.getEmail())
                .password(passwordEncoder.encode(register.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponceDTO.builder()
                .token(jwtToken)
                .userEmail(user.getEmail())
                .build();
    }

    public @Nullable AuthenticationResponceDTO login(AuthenticationRequestDTO authenticationRequestDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequestDTO.getUserEmail(),
                        authenticationRequestDTO.getPassword()
                )
        );
        User user = userRepository.findByEmail(authenticationRequestDTO.getUserEmail()).orElseThrow(() -> new UsernameNotFoundException("user not found!!"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponceDTO.builder()
                .token(jwtToken)
                .userEmail(user.getEmail())
                .build();
    }

}
