package com.example.QuickFixersBackend.entity;

import com.example.QuickFixersBackend.enums.Priority;
import com.example.QuickFixersBackend.enums.Statut;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="ticket", indexes = {
        @Index(name = "idx_ticket_assigned_statut", columnList = "assigned_to, statut"),
        @Index(name = "idx_ticket_assigned_priority", columnList = "assigned_to, priority"),
        @Index(name = "idx_ticket_created_statut", columnList = "created_by, statut"),
        @Index(name = "idx_ticket_created_priority", columnList = "created_by, priority"),
        @Index(name = "idx_ticket_statut", columnList = "statut"),
        @Index(name = "idx_ticket_priority", columnList = "priority"),
        @Index(name = "idx_ticket_date_creation", columnList = "date_creation"),
        @Index(name = "idx_ticket_service", columnList = "service_id")
})
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String  titre;
    private String description;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Column(updatable = false)
    private LocalDateTime dateCreation;

    @Enumerated(EnumType.STRING)
    private Statut statut;

    @ManyToOne(optional = true)
    @JoinColumn(name = "assigned_to")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Person assignedTo;

    private BigDecimal prix;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Person createdBy;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity service;

}
