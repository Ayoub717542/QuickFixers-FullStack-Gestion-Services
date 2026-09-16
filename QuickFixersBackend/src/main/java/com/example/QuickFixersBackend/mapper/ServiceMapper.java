package com.example.QuickFixersBackend.mapper;
import com.example.QuickFixersBackend.dto.service.ServiceRequistDTO;
import com.example.QuickFixersBackend.dto.service.ServiceResponseDTO;
import com.example.QuickFixersBackend.entity.ServiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    ServiceEntity toEntity(ServiceRequistDTO dto);
    @Mapping(source="createdBy.id", target="createdById")
    ServiceResponseDTO toDto(ServiceEntity serviceEntity);
}
