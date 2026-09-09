package com.example.QuickFixersBackend.dto.paiement;

import com.example.QuickFixersBackend.enums.PaiementStatut;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaiementResponseDTO {
    private Long id;
    private double montant;
    private PaiementStatut statut;
    private LocalDateTime dateCreation;
    private Long ticketId;
}
