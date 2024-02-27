package com.barabanov.mandatory.model.dbms.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Data
@Builder
public class DeleteTupleDto
{
    Long tableSecId;
    Long tupleId;
}
