package com.example.QuickFixersBackend.mapper;
import com.example.QuickFixersBackend.DTO.ServiceRequistDTO;
import com.example.QuickFixersBackend.DTO.ServiceResponseDTO;
import com.example.QuickFixersBackend.model.ServiceEntity;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface ServiceMapper {
    ServiceEntity toEntity(ServiceRequistDTO dto);
    ServiceResponseDTO toDto(ServiceEntity serviceEntity);
}
