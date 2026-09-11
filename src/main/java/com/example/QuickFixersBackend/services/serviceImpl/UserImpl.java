package com.example.QuickFixersBackend.services.serviceImpl;

import com.example.QuickFixersBackend.dto.user.UserResponseDTO;
import com.example.QuickFixersBackend.entity.User;
import com.example.QuickFixersBackend.enums.Role;
import com.example.QuickFixersBackend.mapper.UserMapper;
import com.example.QuickFixersBackend.repository.UserRepository;
import com.example.QuickFixersBackend.services.serviceInterfce.UserInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public record UserImpl(UserRepository userRepository, UserMapper userMapper) implements UserInterface {
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
}
