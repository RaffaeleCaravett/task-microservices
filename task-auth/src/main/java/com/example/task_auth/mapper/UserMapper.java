package com.example.task_auth.mapper;

import com.example.task_auth.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
config = MapperConfiguration.class)
public interface UserMapper {

}
