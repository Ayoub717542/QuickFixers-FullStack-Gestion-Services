package com.example.QuickFixersBackend.entity;

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
@Table(name="ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String  titre;
    private String description;


    @Column(updatable = false)
    private LocalDateTime dateCreation;

    @Enumerated(EnumType.STRING)
    private Statut statut;

    @ManyToOne(optional = true)
    @JoinColumn(name = "assigned_to")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User assignedTo;

    private BigDecimal prix;

    @ManyToOne
    @JoinColumn(name = "created_by")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity service;

}
