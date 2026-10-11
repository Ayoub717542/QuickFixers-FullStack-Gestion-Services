package com.example.QuickFixersBackend.dto.ticket;

import com.example.QuickFixersBackend.enums.Priority;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequestDTO {
    private String titre;
    private String description;
    private Priority priority;
}
