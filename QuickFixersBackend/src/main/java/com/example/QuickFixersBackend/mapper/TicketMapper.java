package com.example.QuickFixersBackend.mapper;

import com.example.QuickFixersBackend.dto.ticket.TicketRequestDTO;
import com.example.QuickFixersBackend.dto.ticket.TicketResponseDTO;
import com.example.QuickFixersBackend.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {
Ticket toEntity(TicketRequestDTO dto);
@Mapping(source = "assignedTo.id", target = "assignedToId")
@Mapping(source = "assignedTo.nom", target = "assignedToNom")
@Mapping(source = "assignedTo.prenom", target = "assignedToPrenom")
@Mapping(source = "service.id", target = "serviceId")
@Mapping(source = "createdBy.id", target = "createdById")
@Mapping(target = "priorityLabel", ignore = true)
@Mapping(target = "slaHeures", ignore = true)
@Mapping(target = "echeance", ignore = true)
@Mapping(target = "enRetard", ignore = true)
TicketResponseDTO toDto(Ticket ticket);
}
