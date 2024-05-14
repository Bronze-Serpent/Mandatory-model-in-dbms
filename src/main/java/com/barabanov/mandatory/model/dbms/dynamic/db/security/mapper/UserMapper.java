package com.barabanov.mandatory.model.dbms.dynamic.db.security.mapper;

import com.barabanov.mandatory.model.dbms.controller.rest.dto.CreateUserDto;
import com.barabanov.mandatory.model.dbms.controller.rest.dto.UserReadDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.DbmsAdmin;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.DbmsUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;


@Mapper(componentModel = SPRING)
public interface UserMapper
{

    @Mapping(target = "id", ignore = true)
    DbmsUser toUser(CreateUserDto createUserDto);

    @Mapping(target = "databases", ignore = true)
    @Mapping(target = "administeredDatabases", ignore = true)
    DbmsAdmin toAdmin(CreateUserDto createUserDto);

    @Mapping(target = "authority", constant = "USER")
    UserReadDto toUserDto(DbmsUser user);

    @Mapping(target = "authority", constant = "ADMIN")
    UserReadDto toUserDto(DbmsAdmin admin);
}
