package com.example.QuickFixersBackend.model;

import com.example.QuickFixersBackend.enums.ServiceStatut;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "services")
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String description;

    private double prix;

    private String dureeEstimee;

    @Enumerated(EnumType.STRING)
    private ServiceStatut statut;


    @OneToMany(mappedBy = "service")
    private List<Ticket> tickets;
}