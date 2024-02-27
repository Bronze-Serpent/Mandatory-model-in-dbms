package com.barabanov.mandatory.model.dbms.dynamic.db.security.mapper;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadColumnSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.ColumnSecurity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;


@Mapper(componentModel = SPRING)
public interface ColumnSecurityMapper
{
    @Mapping(target = "secId", source = "id")
    @Mapping(target = "tableSecId", source = "tableSecurity.id")
    ReadColumnSecDto toDto(ColumnSecurity columnSecurity);
}
