package com.example.QuickFixersBackend.services.serviceImpl;

import com.example.QuickFixersBackend.dto.support.CreateSupportRequestDTO;
import com.example.QuickFixersBackend.dto.user.*;
import com.example.QuickFixersBackend.entity.Admin;
import com.example.QuickFixersBackend.entity.Client;
import com.example.QuickFixersBackend.entity.Person;
import com.example.QuickFixersBackend.entity.Support;
import com.example.QuickFixersBackend.enums.ServiceType;
import com.example.QuickFixersBackend.mapper.UserMapper;
import com.example.QuickFixersBackend.repository.UserRepository;
import com.example.QuickFixersBackend.services.serviceInterfce.UserInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserImpl implements UserInterface {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

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

    @Override
    @Transactional
    public UserResponseDTO changerRole(Long id, String role, ServiceType serviceType) {
        Person person = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (person instanceof Admin) {
            throw new RuntimeException("Le rôle d'un administrateur ne peut pas être modifié");
        }
        if (!role.equals("CLIENT") && !role.equals("SUPPORT")) {
            throw new RuntimeException("Rôle invalide : seuls CLIENT et SUPPORT sont autorisés");
        }

        if (role.equals("CLIENT")) {
            userRepository.changerRole(id, "CLIENT", null);
        } else {
            if (serviceType == null) {
                throw new RuntimeException("Un type de service est requis pour un compte SUPPORT");
            }
            userRepository.changerRole(id, "SUPPORT", serviceType.name());
        }
        Person updated = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(updated);
    }

    @Override
    public UserResponseDTO modifierUser(Long id, UserEditRequestDTO dto) {
        Person person = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (person instanceof Admin) {
            throw new RuntimeException("Le compte d'un administrateur ne peut pas être modifié");
        }
        if (!person.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }
        person.setNom(dto.getNom());
        person.setPrenom(dto.getPrenom());
        person.setEmail(dto.getEmail());

        return userMapper.toDto(userRepository.save(person));
    }
    @Override
    public UserResponseDTO consulterUser(Long id) {
        Person person = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(person);
    }

    @Override
    public Page<UserResponseDTO> listerClients(Pageable pageable) {
        return userRepository.findAllClients(pageable)
                .map(userMapper::toDto);
    }

    @Override
    public Page<UserResponseDTO> listerSupports(Pageable pageable) {
        return userRepository.findAllSupports(pageable)
                .map(userMapper::toDto);
    }


    @Override
    public Page<UserResponseDTO> rechercherSupports(String searchedEmail, Pageable pageable) {
        return userRepository.searchedSupport(searchedEmail, pageable)
                .map(userMapper::toDto);
    }

    @Override
    public Page<UserResponseDTO> rechercherClients(String nom, Pageable pageable) {
        return userRepository.rechercherClients(nom.trim(), pageable)
                .map(userMapper::toDto);
    }

    @Override
    public Page<UserResponseDTO> filtrerSupportsParService(ServiceType serviceType, Pageable pageable) {
        return userRepository.findSupportsByServiceType(serviceType, pageable)
                .map(userMapper::toDto);
    }


}