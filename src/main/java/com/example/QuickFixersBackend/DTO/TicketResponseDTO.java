package com.example.QuickFixersBackend.DTO;

import com.example.QuickFixersBackend.enums.Statut;
import com.example.QuickFixersBackend.model.ServiceEntity;
import com.example.QuickFixersBackend.model.User;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseDTO {

    private Long id;
    private String  titre;
    private String description;
    private Statut statut;
    private LocalDate dateCreation;
    private User assignedTo;
    private double prix;
    private ServiceEntity serviceEntity;
}
