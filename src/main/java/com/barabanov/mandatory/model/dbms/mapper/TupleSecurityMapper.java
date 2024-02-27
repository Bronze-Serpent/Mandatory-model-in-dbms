package com.barabanov.mandatory.model.dbms.mapper;

import com.barabanov.mandatory.model.dbms.service.dto.ReadTupleSecurityDto;
import com.barabanov.mandatory.model.dbms.entity.TupleSecurity;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;


@Mapper(componentModel = SPRING)
public interface TupleSecurityMapper
{

    ReadTupleSecurityDto toDto(TupleSecurity tupleSecurity);
}
