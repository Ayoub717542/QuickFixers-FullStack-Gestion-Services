package com.example.QuickFixersBackend.dto.ticket;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequestDTO {
    private String titre;
    private String description;
}