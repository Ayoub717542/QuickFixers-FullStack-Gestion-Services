package com.example.QuickFixersBackend.entity;

import com.example.QuickFixersBackend.enums.Statut;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String  titre;
    private String description;
    private LocalDate dateCreation;
    private double prix;

    @Enumerated(EnumType.STRING)
    private Statut statut;

    @ManyToOne(optional = true)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity service;

}
