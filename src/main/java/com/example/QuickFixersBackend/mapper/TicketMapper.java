package com.example.QuickFixersBackend.mapper;

import com.example.QuickFixersBackend.dto.ticket.TicketRequestDTO;
import com.example.QuickFixersBackend.dto.ticket.TicketResponseDTO;
import com.example.QuickFixersBackend.entity.Ticket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper {
Ticket toEntity(TicketRequestDTO dto);
TicketResponseDTO toDto (Ticket ticket);
}
