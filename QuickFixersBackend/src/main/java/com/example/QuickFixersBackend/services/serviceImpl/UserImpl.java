package com.example.QuickFixersBackend.services.serviceImpl;

import com.example.QuickFixersBackend.dto.support.CreateSupportRequestDTO;
import com.example.QuickFixersBackend.dto.user.UserRequestDTO;
import com.example.QuickFixersBackend.dto.user.UserResponseDTO;
import com.example.QuickFixersBackend.dto.user.UserUpdateRequestDTO;
import com.example.QuickFixersBackend.entity.User;
import com.example.QuickFixersBackend.enums.Role;
import com.example.QuickFixersBackend.mapper.UserMapper;
import com.example.QuickFixersBackend.repository.UserRepository;
import com.example.QuickFixersBackend.services.serviceInterfce.UserInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public record UserImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder
) implements UserInterface {

    @Override
    public UserResponseDTO ajouterUnUser(UserRequestDTO userRequestDTO) {

      if(userRepository.existsByEmail(userRequestDTO.getEmail())){
          throw new RuntimeException("This user already exists");
      }
        User user= User.builder()
                .nom(userRequestDTO.getNom())
                .prenom(userRequestDTO.getPrenom())
                .email(userRequestDTO.getEmail())
                .password(passwordEncoder.encode(userRequestDTO.getPassword()))
                .role(Role.USER)
                .build();
        return  userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDTO changerRole(Long id, Role role) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found"));
        user.setRole(role);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public Page<UserResponseDTO> listerUsers(Pageable pageable) {
        return  userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }

    @Override
    public void supprimerUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found to delete"));
        userRepository.delete(user);
    }

    public UserResponseDTO createSupportAccount(CreateSupportRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }

        User support = User.builder()
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.SUPPORT)
                .serviceType(dto.getServiceType())
                .build();

        return userMapper.toDto(userRepository.save(support));
    }

    @Override
    public UserResponseDTO monProfil(User user) {
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDTO modifierProfil(User user, UserUpdateRequestDTO dto) {
        user.setNom(dto.getNom());
        user.setPrenom(dto.getPrenom());
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public long countUsers() {
        return userRepository.count();
    }
}
