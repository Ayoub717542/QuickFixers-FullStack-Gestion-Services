package com.example.QuickFixersBackend.dto.paiement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaiementRequestDTO {
    private double montant;
    private Long ticketId;
}
