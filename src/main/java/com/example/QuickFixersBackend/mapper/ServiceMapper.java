package com.example.QuickFixersBackend.mapper;
import com.example.QuickFixersBackend.dto.service.ServiceRequistDTO;
import com.example.QuickFixersBackend.dto.service.ServiceResponseDTO;
import com.example.QuickFixersBackend.entity.ServiceEntity;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface ServiceMapper {
    ServiceEntity toEntity(ServiceRequistDTO dto);
    ServiceResponseDTO toDto(ServiceEntity serviceEntity);
}
