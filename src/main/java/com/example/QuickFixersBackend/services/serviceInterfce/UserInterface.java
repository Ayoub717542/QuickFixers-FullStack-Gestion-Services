package com.example.QuickFixersBackend.services.serviceInterfce;

import com.example.QuickFixersBackend.dto.support.CreateSupportRequestDTO;
import com.example.QuickFixersBackend.dto.user.UserRequestDTO;
import com.example.QuickFixersBackend.dto.user.UserResponseDTO;
import com.example.QuickFixersBackend.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserInterface {

    UserResponseDTO ajouterUnUser(UserRequestDTO userRequestDTO);
    UserResponseDTO changerRole(Long id, Role role);
   Page<UserResponseDTO> listerUsers(Pageable pageable);
   void supprimerUser(Long id);
   UserResponseDTO createSupportAccount(CreateSupportRequestDTO dto);
}
