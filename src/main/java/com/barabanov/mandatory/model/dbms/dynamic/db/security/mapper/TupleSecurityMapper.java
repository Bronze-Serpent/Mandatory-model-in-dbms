package com.barabanov.mandatory.model.dbms.dynamic.db.security.mapper;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadTupleSecurityDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.TupleSecurity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;


@Mapper(componentModel = SPRING)
public interface TupleSecurityMapper
{
    @Mapping(target = "tableSecId", source = "tableSecurity.id")
    ReadTupleSecurityDto toDto(TupleSecurity tupleSecurity);
}
