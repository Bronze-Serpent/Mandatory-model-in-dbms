package com.barabanov.mandatory.model.dbms.controller.rest.dto;

import com.barabanov.mandatory.model.dbms.controller.rest.validation.ChangeTupleSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@ChangeTupleSecurity
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Data
public class UpdateTupleSecDto
{
    @NotNull
    Long tableSecId;

    @NotNull
    Long tupleId;

    @NotNull
    SecurityLevel newSecurityLvl;
}
