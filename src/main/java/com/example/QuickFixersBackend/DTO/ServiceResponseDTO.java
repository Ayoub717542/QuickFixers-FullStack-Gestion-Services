package com.example.QuickFixersBackend.DTO;

import com.example.QuickFixersBackend.enums.Statut;
import com.example.QuickFixersBackend.model.Service;
import com.example.QuickFixersBackend.model.User;

import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponseDTO {
    private Long id;
    private String  titre;
    private String description;
    private Statut statut;
    private LocalDate dateCreation;
    private double prix;
    private User assignedTo;
    private Service service;
}
