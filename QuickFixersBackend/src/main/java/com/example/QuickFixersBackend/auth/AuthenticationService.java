package com.example.QuickFixersBackend.auth;

import com.example.QuickFixersBackend.dto.auth.AuthenticationRequestDTO;
import com.example.QuickFixersBackend.dto.auth.AuthenticationResponceDTO;
import com.example.QuickFixersBackend.dto.auth.RegisterRequoestDTO;

import com.example.QuickFixersBackend.dto.auth.ResetPasswordRequestDTO;
import com.example.QuickFixersBackend.entity.Client;
import com.example.QuickFixersBackend.entity.Person;
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
        Client user = new Client(
                register.getNom(),
                register.getPrenom(),
                register.getEmail(),
                passwordEncoder.encode(register.getPassword())
        );
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
        Person user = userRepository.findByEmail(authenticationRequestDTO.getUserEmail()).orElseThrow(() -> new UsernameNotFoundException("user not found!!"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponceDTO.builder()
                .token(jwtToken)
                .userEmail(user.getEmail())
                .build();
    }

    public void resetPassword(ResetPasswordRequestDTO dto) {
        return;
    }

}
