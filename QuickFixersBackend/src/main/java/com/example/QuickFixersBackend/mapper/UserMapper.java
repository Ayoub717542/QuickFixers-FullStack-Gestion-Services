package com.example.QuickFixersBackend.mapper;

import com.example.QuickFixersBackend.dto.user.UserResponseDTO;
import com.example.QuickFixersBackend.entity.Admin;
import com.example.QuickFixersBackend.entity.Person;
import com.example.QuickFixersBackend.entity.Support;
import com.example.QuickFixersBackend.enums.ServiceType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", expression = "java(roleOf(person))")
    @Mapping(target = "serviceType", expression = "java(serviceTypeOf(person))")
    UserResponseDTO toDto(Person person);

    default String roleOf(Person person) {
        if (person instanceof Admin) {
            return "ADMIN";
        }
        if (person instanceof Support) {
            return "SUPPORT";
        }
        return "CLIENT";
    }

    default ServiceType serviceTypeOf(Person person) {
        return person instanceof Support support ? support.getServiceType() : null;
    }
}