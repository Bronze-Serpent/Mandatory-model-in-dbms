package com.barabanov.mandatory.model.dbms.controller.rest.dto;

import com.barabanov.mandatory.model.dbms.controller.rest.validation.ChangeValueSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@ChangeValueSecurity
@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UpdateValueSecDto
{
    @NotNull
    Long columnSecId;

    @NotNull
    Long tupleId;

    @NotNull
    SecurityLevel securityLevel;
}
