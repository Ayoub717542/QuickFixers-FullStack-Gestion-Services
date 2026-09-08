package com.example.QuickFixersBackend.controller;

import com.example.QuickFixersBackend.DTO.ServiceRequistDTO;
import com.example.QuickFixersBackend.DTO.ServiceResponseDTO;
import com.example.QuickFixersBackend.enums.ServiceStatut;
import com.example.QuickFixersBackend.repository.ServiceRepository;
import com.example.QuickFixersBackend.services.serviceInterfce.ServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/service")
public class ServiceController {

    private final ServiceInterface serviceInterface;
    private final ServiceRepository serviceRepository;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/ajouterService")
    public ResponseEntity<ServiceResponseDTO> ajouterService(@RequestBody ServiceRequistDTO serviceRequistDTO){
        return ResponseEntity.ok(serviceInterface.ajouterService(serviceRequistDTO));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/supprimerService/{id}")
    public void supprimerService(@PathVariable Long id){
        serviceInterface.supprimerService(id);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("listerServices")
    public  ResponseEntity<Page<ServiceResponseDTO>> listerServices(
            @RequestParam (defaultValue = "1") int pageNumber,
            @RequestParam (defaultValue = "5") int pageSize,
            @RequestParam (defaultValue = "id") String sortBy,
            @RequestParam (defaultValue = "asc") String  sortDir
    ){
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber-1,pageSize,sort);
        Page<ServiceResponseDTO> rs =serviceInterface.listerServices(pageable);
        return  ResponseEntity.ok(rs);
    }

@PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/consulterUnService/{id}")
    public ResponseEntity<ServiceResponseDTO> consulterUnService(@PathVariable Long id){
        return  ResponseEntity.ok(serviceInterface.consulterUnService(id));
}

@PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/modefieStatut/{id}")
    public ResponseEntity<ServiceResponseDTO> modefieStatut(@PathVariable Long id , @RequestBody ServiceStatut statut){
    return  ResponseEntity.ok(serviceInterface.updateStatus(id,statut));
}
}
