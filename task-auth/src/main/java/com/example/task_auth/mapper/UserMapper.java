package com.example.task_auth.mapper;

import com.example.task_auth.dto.UserDTO;
import com.example.task_auth.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
config = MapperConfiguration.class)
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    UserDTO toUserDTO(@MappingTarget UserDTO userDTO, User user);
}
