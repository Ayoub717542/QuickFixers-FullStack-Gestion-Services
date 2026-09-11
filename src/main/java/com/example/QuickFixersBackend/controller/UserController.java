package com.example.QuickFixersBackend.controller;

import com.example.QuickFixersBackend.dto.user.UserResponseDTO;
import com.example.QuickFixersBackend.enums.Role;
import com.example.QuickFixersBackend.services.serviceInterfce.UserInterface;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")

public class UserController {

    private final UserInterface userInterface;

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/changeRole/{id}")
    public ResponseEntity<?> changerRole(@PathVariable Long id, @RequestParam Role role) {
        return ResponseEntity.ok(userInterface.changerRole(id, role));
    }

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
}
