package com.example.QuickFixersBackend.services.serviceInterfce;

import com.example.QuickFixersBackend.dto.support.CreateSupportRequestDTO;
import com.example.QuickFixersBackend.dto.user.UserRequestDTO;
import com.example.QuickFixersBackend.dto.user.UserResponseDTO;
import com.example.QuickFixersBackend.dto.user.UserUpdateRequestDTO;
import com.example.QuickFixersBackend.entity.Person;
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
}