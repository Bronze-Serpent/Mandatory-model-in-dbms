package com.barabanov.mandatory.model.dbms.dynamic.db.security.mapper;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadTableSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.TableSecurity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;


@Mapper(componentModel = SPRING)
public interface TableSecurityMapper
{
    @Mapping(target = "dbSecId", source = "databaseSecurity.id")
    @Mapping(target = "secId", source = "id")
    ReadTableSecDto toDto(TableSecurity tableSecurity);
}
