package com.example.QuickFixersBackend.mapper;

import com.example.QuickFixersBackend.DTO.TicketRequestDTO;
import com.example.QuickFixersBackend.DTO.TicketResponseDTO;
import com.example.QuickFixersBackend.model.Ticket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper {
Ticket toEntity(TicketRequestDTO dto);
TicketResponseDTO toDto (Ticket ticket);
}
