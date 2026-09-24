package com.example.QuickFixersBackend.controller;

import com.example.QuickFixersBackend.dto.paiement.IncomeByDay;
import com.example.QuickFixersBackend.dto.paiement.PaiementRequestDTO;
import com.example.QuickFixersBackend.dto.paiement.PaiementResponseDTO;
import com.example.QuickFixersBackend.entity.Person;
import com.example.QuickFixersBackend.services.serviceInterfce.PaiementInterface;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("api/paiements")
@EnableMethodSecurity

public class PaiementController {
    private final PaiementInterface paiementInterface;

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/effectuerPaiement")
    public ResponseEntity<PaiementResponseDTO> effectuerPaiement(
            @RequestBody PaiementRequestDTO paiementRequestDTO,
            @AuthenticationPrincipal Person person
    ){
        return ResponseEntity.ok(paiementInterface.creerPaiement(paiementRequestDTO,person.getEmail()));
    }

    @PreAuthorize(("hasAnyRole('ADMIN','CLIENT','SUPPORT')"))
    @GetMapping("/paimentHistorique")
    public  ResponseEntity<Page<PaiementResponseDTO>> paimentHistorique(
            @AuthenticationPrincipal Person person,
            @RequestParam (defaultValue = "1") int pageNumber,
            @RequestParam (defaultValue = "5") int pageSize,
            @RequestParam (defaultValue = "id") String sortBy,
            @RequestParam (defaultValue = "asc") String  sortDir

    ){
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNumber-1,pageSize,sort);
        Page<PaiementResponseDTO> rs = paiementInterface.paimentHistorique(person,pageable);

        return ResponseEntity.ok(rs);

    }

    @GetMapping("/paiements")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT')")
    public ResponseEntity<Long> userCountPaiements(@AuthenticationPrincipal Person person){
        return ResponseEntity.ok(paiementInterface.countPayments(person));
    }

    @GetMapping("/incomeByDay")
    @PreAuthorize("hasAnyRole('ADMIN','SUPPORT')")
    public ResponseEntity<List<IncomeByDay>> revenusParJour(@AuthenticationPrincipal Person person) {
        return ResponseEntity.ok(paiementInterface.incomeByday(person));
    }
}