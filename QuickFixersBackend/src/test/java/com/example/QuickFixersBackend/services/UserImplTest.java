package com.example.QuickFixersBackend.services;

import com.example.QuickFixersBackend.dto.user.UserRequestDTO;
import com.example.QuickFixersBackend.dto.user.UserResponseDTO;
import com.example.QuickFixersBackend.entity.Client;
import com.example.QuickFixersBackend.mapper.UserMapper;
import com.example.QuickFixersBackend.repository.UserRepository;
import com.example.QuickFixersBackend.services.serviceImpl.EmailService;
import com.example.QuickFixersBackend.services.serviceImpl.UserImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private EmailService emailService;

    private UserImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserImpl(userRepository, userMapper, passwordEncoder, emailService);
    }

    @Test
    void creerUtilisateur_ok() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setNom("Hadi");
        dto.setPrenom("Ayoub");
        dto.setEmail("ayoub@gmail.com");
        dto.setPassword("ayoub123");

        when(userRepository.existsByEmail("ayoub@gmail.com"))
                .thenReturn(false);
        when(passwordEncoder.encode("ayoub123"))
                .thenReturn("HASHED");
        when(userRepository.save(any(Client.class)))
                .thenReturn(new Client());
        when(userMapper.toDto(any(Client.class)))
                .thenReturn(new UserResponseDTO());

        userService.ajouterUnUser(dto);

        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(userRepository).save(captor.capture());

        Client saved = captor.getValue();
        assertTrue(saved instanceof Client);
        assertEquals("HASHED", saved.getPassword());
        assertEquals("ayoub@gmail.com", saved.getEmail());
    }

    @Test
    void emailExistant_erreur() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("ayoub@gmail.com");
        dto.setPassword("ayoub123");

        when(userRepository.existsByEmail("ayoub@gmail.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.ajouterUnUser(dto));
        verify(userRepository, never()).save(any());
    }
}