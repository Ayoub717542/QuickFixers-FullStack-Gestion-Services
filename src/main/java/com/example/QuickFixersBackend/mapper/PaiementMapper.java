package com.example.QuickFixersBackend.mapper;

import com.example.QuickFixersBackend.dto.paiement.PaiementRequestDTO;
import com.example.QuickFixersBackend.dto.paiement.PaiementResponseDTO;
import com.example.QuickFixersBackend.entity.Paiement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaiementMapper {
    @Mapping(target = "ticket", ignore = true) // set manually in service, needs a repository lookup
    Paiement toEntity(PaiementRequestDTO dto);
    @Mapping(source = "ticket.id", target = "ticketId")
    PaiementResponseDTO toDto(Paiement payment);
}
