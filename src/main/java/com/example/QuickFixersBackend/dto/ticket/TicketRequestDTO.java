package com.example.QuickFixersBackend.dto.ticket;

import com.example.QuickFixersBackend.enums.Statut;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequestDTO {
    private String titre;
    private String description;
    private Statut statut;
    private double prix;
    private Long assignedToId;
    private Long serviceId;
}