package com.example.QuickFixersBackend.entity;
import com.example.QuickFixersBackend.enums.PaiementStatut;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="paiement", indexes = {
        @Index(name = "idx_paiement_statut_date", columnList = "statut, date_creation"),
        @Index(name = "idx_paiement_ticket_statut", columnList = "ticket_id, statut"),
        @Index(name = "idx_paiement_user", columnList = "user_id")
})
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    private PaiementStatut statut = PaiementStatut.EN_ATTENTE;

    private LocalDateTime dateCreation = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Person client;

}
