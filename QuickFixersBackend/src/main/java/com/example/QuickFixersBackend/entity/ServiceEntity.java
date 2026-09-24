package com.example.QuickFixersBackend.entity;

import com.example.QuickFixersBackend.enums.ServiceStatut;
import com.example.QuickFixersBackend.enums.ServiceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
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

    @Enumerated(EnumType.STRING)
    private ServiceStatut statut;

    @Enumerated(EnumType.STRING)
    private ServiceType type;


    @OneToMany(mappedBy = "service")
    private List<Ticket> tickets;

    private BigDecimal prix;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private Person createdBy;

}