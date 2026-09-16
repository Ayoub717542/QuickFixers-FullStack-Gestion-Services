package com.example.QuickFixersBackend.mapper;

import com.example.QuickFixersBackend.dto.user.UserRequestDTO;
import com.example.QuickFixersBackend.dto.user.UserResponseDTO;
import com.example.QuickFixersBackend.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequestDTO userRequestDTO);
    UserResponseDTO toDto (User user);
}
