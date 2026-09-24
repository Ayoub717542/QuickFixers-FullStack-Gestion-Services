package com.example.QuickFixersBackend.services.serviceInterfce;

import com.example.QuickFixersBackend.dto.support.CreateSupportRequestDTO;
import com.example.QuickFixersBackend.dto.user.UserEditRequestDTO;
import com.example.QuickFixersBackend.dto.user.UserRequestDTO;
import com.example.QuickFixersBackend.dto.user.UserResponseDTO;
import com.example.QuickFixersBackend.dto.user.UserUpdateRequestDTO;
import com.example.QuickFixersBackend.entity.Person;
import com.example.QuickFixersBackend.enums.ServiceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserInterface {

    UserResponseDTO ajouterUnUser(UserRequestDTO userRequestDTO);
   Page<UserResponseDTO> listerUsers(Pageable pageable);
   void supprimerUser(Long id);
   UserResponseDTO createSupportAccount(CreateSupportRequestDTO dto);
   UserResponseDTO monProfil(Person person);
   UserResponseDTO modifierProfil(Person person, UserUpdateRequestDTO dto);
    long countUsers();
    UserResponseDTO changerRole(Long id, String role, ServiceType serviceType);
    UserResponseDTO modifierUser(Long id, UserEditRequestDTO dto);
    UserResponseDTO consulterUser(Long id);
    Page<UserResponseDTO> listerClients(Pageable pageable);
    Page<UserResponseDTO> listerSupports(Pageable pageable);
    Page<UserResponseDTO> rechercherSupports(String searchedEmail, Pageable pageable);
    Page<UserResponseDTO> filtrerSupportsParService(ServiceType serviceType, Pageable pageable);
    Page<UserResponseDTO> rechercherClients(String search, Pageable pageable);
    Page<UserResponseDTO> rechercherUsers(String keyword, Pageable pageable);
    Page<UserResponseDTO> filtrerUsers(String role, Pageable pageable);


}