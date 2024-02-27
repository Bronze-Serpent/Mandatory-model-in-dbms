package com.barabanov.mandatory.model.dbms.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CreateTupleDto
{
    Long dbSecId;
    String secureSql;
}
