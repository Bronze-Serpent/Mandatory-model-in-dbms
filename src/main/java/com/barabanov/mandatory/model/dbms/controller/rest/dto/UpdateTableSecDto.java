package com.barabanov.mandatory.model.dbms.controller.rest.dto;

import com.barabanov.mandatory.model.dbms.controller.rest.validation.ChangeTableSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@ChangeTableSecurity
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Data
public class UpdateTableSecDto
{
    @NotNull
    Long tableSecId;

    @NotNull
    SecurityLevel securityLevel;
}
