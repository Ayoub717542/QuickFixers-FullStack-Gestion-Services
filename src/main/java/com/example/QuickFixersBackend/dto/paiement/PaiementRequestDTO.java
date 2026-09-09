package com.example.QuickFixersBackend.dto.paiement;

import com.example.QuickFixersBackend.entity.Ticket;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaiementRequestDTO {
    private double montant;
    private Long ticketId;
}
