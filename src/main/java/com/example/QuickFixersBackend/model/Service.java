package com.example.QuickFixersBackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String description;

    private double prix;

    private String dureeEstimee;

    private boolean statut;

    @OneToMany(mappedBy = "service")
    private List<Ticket> tickets;
}