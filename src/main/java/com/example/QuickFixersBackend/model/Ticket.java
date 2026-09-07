package com.example.QuickFixersBackend.model;

import com.example.QuickFixersBackend.enums.Statut;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@RequiredArgsConstructor
@Entity

public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String  titre;
    private String description;

    @Enumerated(EnumType.STRING)
    private Statut statut;

    private LocalDate dateCreation;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    private double prix;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

}
