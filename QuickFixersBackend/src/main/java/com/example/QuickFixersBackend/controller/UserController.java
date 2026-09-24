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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserInterface userInterface;

    @GetMapping("/listerUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> listerUsers(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber -1 , pageSize,sort);

        return ResponseEntity.ok(userInterface.listerUsers(pageable));
    }

    @GetMapping("/listerClients")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> listerClients(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        return ResponseEntity.ok(userInterface.listerClients(pageable));
    }

    @GetMapping("/listerSupports")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> listerSupports(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        Pageable pageable =  creerPageable(pageNumber, pageSize);

        return ResponseEntity.ok(userInterface.listerSupports(pageable));
    }


    @GetMapping("/rechercherSupports")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> rechercherSupports(
            @RequestParam String searchedEmail,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        Pageable pageable = creerPageable(pageNumber, pageSize);

        return ResponseEntity.ok(
                userInterface.rechercherSupports(searchedEmail, pageable)
        );
    }

    @GetMapping("/filtrerSupports")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> filtrerSupports(
            @RequestParam ServiceType serviceType,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        Pageable pageable =  creerPageable(pageNumber, pageSize);

        return ResponseEntity.ok(
                userInterface.filtrerSupportsParService(serviceType, pageable)
        );
    }

    @GetMapping("/consulterUser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> consulterUser(@PathVariable Long id) {
        return ResponseEntity.ok(userInterface.consulterUser(id));
    }

    @PostMapping("/ajouterUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> ajouterUnUser(
            @Valid @RequestBody UserRequestDTO dto
    ) {
        return ResponseEntity.ok(userInterface.ajouterUnUser(dto));
    }

    @PostMapping("/Ajoutersupport")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createSupport(
            @Valid @RequestBody CreateSupportRequestDTO dto
    ) {
        UserResponseDTO created = userInterface.createSupportAccount(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/modifierUser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> modifierUser(
            @PathVariable Long id,
            @Valid @RequestBody UserEditRequestDTO dto
    ) {
        return ResponseEntity.ok(userInterface.modifierUser(id, dto));
    }

    @PatchMapping("/changeRole/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> changerRole(
            @PathVariable Long id,
            @RequestParam String role,
            @RequestParam(required = false) ServiceType serviceType
    ) {
        return ResponseEntity.ok(
                userInterface.changerRole(id, role, serviceType)
        );
    }

    @DeleteMapping("/supprimerUser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void supprimerUser(@PathVariable Long id) {
        userInterface.supprimerUser(id);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'SUPPORT')")
    public ResponseEntity<UserResponseDTO> monProfil(
            @AuthenticationPrincipal Person person
    ) {
        return ResponseEntity.ok(userInterface.monProfil(person));
    }

    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'SUPPORT')")
    public ResponseEntity<UserResponseDTO> modifierProfil(
            @AuthenticationPrincipal Person person,
            @Valid @RequestBody UserUpdateRequestDTO dto
    ) {
        return ResponseEntity.ok(
                userInterface.modifierProfil(person, dto)
        );
    }

    @GetMapping("/rechercherClients")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> rechercherClients(
            @RequestParam String nom,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        Pageable pageable = creerPageable(pageNumber, pageSize);

        return ResponseEntity.ok(
                userInterface.rechercherClients(nom, pageable)
        );
    }

    @GetMapping("/countUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getUsers() {
        return ResponseEntity.ok(userInterface.countUsers());
    }

    private Pageable creerPageable(int pageNumber, int pageSize) {
        return PageRequest.of(pageNumber - 1, pageSize);
    }

}