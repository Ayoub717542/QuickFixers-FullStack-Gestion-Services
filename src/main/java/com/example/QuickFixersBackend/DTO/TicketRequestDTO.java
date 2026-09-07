package com.example.QuickFixersBackend.DTO;

import com.example.QuickFixersBackend.enums.Statut;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequestDTO {
    private String  titre;
    private String description;
    private Statut statut;
    private LocalDate dateCreation;
}