package com.example.QuickFixersBackend.dto.service;

import com.example.QuickFixersBackend.enums.ServiceStatut;

import com.example.QuickFixersBackend.enums.ServiceType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponseDTO {
    private Long id;
    private String nom;
    private ServiceStatut statut;
    private ServiceType type;
    private BigDecimal prix;
    private Long createdById;
}
