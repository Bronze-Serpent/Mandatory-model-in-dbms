package com.barabanov.mandatory.model.dbms.mapper;

import com.barabanov.mandatory.model.dbms.controller.dto.ReadValueSecDto;
import com.barabanov.mandatory.model.dbms.entity.ValueSecurity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;


@Mapper(componentModel = SPRING)
public interface ValueSecurityMapper
{
    @Mapping(target = "secId", source = "id")
    @Mapping(target = "columnSecId", source = "columnSecurity.id")
    ReadValueSecDto toDto(ValueSecurity valueSecurity);
}
