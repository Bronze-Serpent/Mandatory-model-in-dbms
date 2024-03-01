package com.barabanov.mandatory.model.dbms.controller.rest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Data
@Builder
public class DeleteTupleDto
{
    @NotNull
    Long tableSecId;

    @NotNull
    Long tupleId;
}
