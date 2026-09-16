package com.example.QuickFixersBackend.controller;

import com.example.QuickFixersBackend.dto.paiement.PaiementRequestDTO;
import com.example.QuickFixersBackend.dto.paiement.PaiementResponseDTO;
import com.example.QuickFixersBackend.entity.User;
import com.example.QuickFixersBackend.services.serviceInterfce.PaiementInterface;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("api/paiements")
public class PaiementController {
    private final PaiementInterface paiementInterface;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/effectuerPaiement")
    public ResponseEntity<PaiementResponseDTO> effectuerPaiement(
            @RequestBody PaiementRequestDTO paiementRequestDTO,
            @AuthenticationPrincipal User user
    ){
        return ResponseEntity.ok(paiementInterface.creerPaiement(paiementRequestDTO,user.getEmail()));
    }

    @PreAuthorize(("hasRole('ADMIN')"))
    @GetMapping("/paimentHistorique")
    public  ResponseEntity<Page<PaiementResponseDTO>> paimentHistorique(
            @AuthenticationPrincipal User user,
            @RequestParam (defaultValue = "1") int pageNumber,
            @RequestParam (defaultValue = "5") int pageSize,
            @RequestParam (defaultValue = "id") String sortBy,
            @RequestParam (defaultValue = "asc") String  sortDir

    ){
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNumber-1,pageSize,sort);
        Page<PaiementResponseDTO> rs = paiementInterface.paimentHistorique(user,pageable);

        return ResponseEntity.ok(rs);

    }

    @GetMapping("/paiements")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Long> userCountPaiements(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(paiementInterface.countPayments(user));
    }


}
