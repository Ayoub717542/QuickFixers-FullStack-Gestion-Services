package com.example.QuickFixersBackend.controller;

import com.example.QuickFixersBackend.dto.support.CreateSupportRequestDTO;
import com.example.QuickFixersBackend.dto.user.UserEditRequestDTO;
import com.example.QuickFixersBackend.dto.user.UserRequestDTO;
import com.example.QuickFixersBackend.dto.user.UserResponseDTO;
import com.example.QuickFixersBackend.dto.user.UserUpdateRequestDTO;
import com.example.QuickFixersBackend.entity.Person;
import com.example.QuickFixersBackend.enums.ServiceType;
import com.example.QuickFixersBackend.services.serviceInterfce.UserInterface;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@EnableMethodSecurity

public class UserController {

    private final UserInterface userInterface;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listerUsers")
    public ResponseEntity<Page<UserResponseDTO>>  listerUsers(
            @RequestParam (defaultValue = "1") int pageNumber,
            @RequestParam (defaultValue = "5") int pageSize,
            @RequestParam (defaultValue = "id") String sortBy,
            @RequestParam (defaultValue = "asc") String  sortDir
    ){
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber-1,pageSize,sort);
        Page<UserResponseDTO> rs = userInterface.listerUsers(pageable);
        return ResponseEntity.ok(rs);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/supprimerUser/{id}")
    public void supprimerUser(@PathVariable Long id){
        userInterface.supprimerUser(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/ajouterUser")
    public ResponseEntity<UserResponseDTO> ajouterUnUser(@Valid @RequestBody UserRequestDTO userRequestDTO){
        return ResponseEntity.ok(userInterface.ajouterUnUser(userRequestDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/changeRole/{id}")
    public ResponseEntity<UserResponseDTO> changerRole(
            @PathVariable Long id,
            @RequestParam String role,
            @RequestParam(required = false) ServiceType serviceType) {
        return ResponseEntity.ok(userInterface.changerRole(id, role, serviceType));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/consulterUser/{id}")
    public ResponseEntity<UserResponseDTO> consulterUser(@PathVariable Long id) {
        return ResponseEntity.ok(userInterface.consulterUser(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/modifierUser/{id}")
    public ResponseEntity<UserResponseDTO> modifierUser(
            @PathVariable Long id,
            @Valid @RequestBody UserEditRequestDTO dto) {
        return ResponseEntity.ok(userInterface.modifierUser(id, dto));
    }

    @PostMapping("/Ajoutersupport")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createSupport(@Valid @RequestBody CreateSupportRequestDTO dto) {
        UserResponseDTO created = userInterface.createSupportAccount(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT','SUPPORT')")
    public ResponseEntity<UserResponseDTO> monProfil(@AuthenticationPrincipal Person person) {
        return ResponseEntity.ok(userInterface.monProfil(person));
    }

    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT','SUPPORT')")
    public ResponseEntity<UserResponseDTO> modifierProfil(
            @AuthenticationPrincipal Person person,
            @Valid @RequestBody UserUpdateRequestDTO dto) {
        return ResponseEntity.ok(userInterface.modifierProfil(person, dto));
    }

    @GetMapping("/countUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getUsers(){
        return ResponseEntity.ok(userInterface.countUsers());
    }

}