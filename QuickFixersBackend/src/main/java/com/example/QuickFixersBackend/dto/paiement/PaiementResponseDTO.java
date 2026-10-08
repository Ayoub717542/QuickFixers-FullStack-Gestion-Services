package com.example.QuickFixersBackend.dto.paiement;

import com.example.QuickFixersBackend.enums.PaiementStatut;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class PaiementResponseDTO implements Serializable {
    private Long id;
    private double montant;
    private PaiementStatut statut;
    private LocalDateTime dateCreation;
    private Long ticketId;
    private String email;
}
