package com.example.QuickFixersBackend.DTO;

import com.example.QuickFixersBackend.enums.ServiceStatut;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponseDTO {
    private Long id;
    private String nom;
    private String description;
    private double prix;
    private String dureeEstimee;
    private ServiceStatut statut;
}
