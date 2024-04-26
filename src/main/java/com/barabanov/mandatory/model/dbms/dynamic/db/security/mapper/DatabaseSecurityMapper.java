package com.barabanov.mandatory.model.dbms.dynamic.db.security.mapper;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadDbSecAdminDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadDbSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.DatabaseSecurity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;


@Mapper(componentModel = SPRING)
public interface DatabaseSecurityMapper
{
    @Mapping(target = "adminsId",
            expression = "java(dbSecurity.getAdmins().stream().map(admin -> admin.getId()).toList())")
    @Mapping(target = "ownerAdminId", source = "owner.id")
    @Mapping(target = "secId", source = "id")
    ReadDbSecAdminDto toAdminDto(DatabaseSecurity dbSecurity);


    @Mapping(target = "secId", source = "id")
    ReadDbSecDto toDto(DatabaseSecurity dbSecurity);
}
