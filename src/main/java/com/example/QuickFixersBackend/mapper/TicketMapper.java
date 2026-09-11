package com.example.QuickFixersBackend.mapper;

import com.example.QuickFixersBackend.dto.ticket.TicketRequestDTO;
import com.example.QuickFixersBackend.dto.ticket.TicketResponseDTO;
import com.example.QuickFixersBackend.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {
Ticket toEntity(TicketRequestDTO dto);
@Mapping(source = "assignedTo.id" ,target = "assignedToId")
@Mapping(source = "service.id" ,target = "serviceId")
@Mapping(source = "createdBy.id", target = "createdById")
TicketResponseDTO toDto (Ticket ticket);

}
