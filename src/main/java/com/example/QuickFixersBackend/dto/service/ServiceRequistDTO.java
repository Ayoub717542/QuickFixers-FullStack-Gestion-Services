package com.example.QuickFixersBackend.dto.service;
import com.example.QuickFixersBackend.enums.ServiceStatut;
import com.example.QuickFixersBackend.enums.ServiceType;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequistDTO {
    private String nom;
    private ServiceStatut statut;
    private ServiceType type;
}
