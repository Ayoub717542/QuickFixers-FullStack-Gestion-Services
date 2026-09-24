package com.example.QuickFixersBackend.services.serviceImpl;

import com.example.QuickFixersBackend.dto.support.CreateSupportRequestDTO;
import com.example.QuickFixersBackend.dto.user.UserRequestDTO;
import com.example.QuickFixersBackend.dto.user.UserResponseDTO;
import com.example.QuickFixersBackend.dto.user.UserUpdateRequestDTO;
import com.example.QuickFixersBackend.entity.Client;
import com.example.QuickFixersBackend.entity.Person;
import com.example.QuickFixersBackend.entity.Support;
import com.example.QuickFixersBackend.mapper.UserMapper;
import com.example.QuickFixersBackend.repository.UserRepository;
import com.example.QuickFixersBackend.services.serviceInterfce.UserInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public record UserImpl(
        UserRepository userRepository,
        UserMapper userMapper,
        PasswordEncoder passwordEncoder,
        EmailService emailService
) implements UserInterface {

    @Override
    public UserResponseDTO ajouterUnUser(UserRequestDTO userRequestDTO) {

      if(userRepository.existsByEmail(userRequestDTO.getEmail())){
          throw new RuntimeException("This user already exists");
      }
        Client user = new Client(
                userRequestDTO.getNom(),
                userRequestDTO.getPrenom(),
                userRequestDTO.getEmail(),
                passwordEncoder.encode(userRequestDTO.getPassword())
        );

        Client saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Override
    public Page<UserResponseDTO> listerUsers(Pageable pageable) {
        return  userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }

    @Override
    public void supprimerUser(Long id) {
        Person person = userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found to delete"));
        userRepository.delete(person);
    }

    public UserResponseDTO createSupportAccount(CreateSupportRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }

        Support support = new Support(
                dto.getNom(),
                dto.getPrenom(),
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getServiceType()
        );

        Support saved = userRepository.save(support);
        return userMapper.toDto(saved);
    }

    @Override
    public UserResponseDTO monProfil(Person person) {
        return userMapper.toDto(person);
    }

    @Override
    public UserResponseDTO modifierProfil(Person person, UserUpdateRequestDTO dto) {
        person.setNom(dto.getNom());
        person.setPrenom(dto.getPrenom());
        return userMapper.toDto(userRepository.save(person));
    }

    @Override
    public long countUsers() {
        return userRepository.count();
    }

}