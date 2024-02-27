package com.barabanov.mandatory.model.dbms.mapper;

import com.barabanov.mandatory.model.dbms.service.dto.ReadTableSecDto;
import com.barabanov.mandatory.model.dbms.entity.TableSecurity;
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
