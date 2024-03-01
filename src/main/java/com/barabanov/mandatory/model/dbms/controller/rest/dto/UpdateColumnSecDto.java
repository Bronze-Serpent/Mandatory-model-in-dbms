package com.barabanov.mandatory.model.dbms.controller.rest.dto;

import com.barabanov.mandatory.model.dbms.controller.rest.validation.ChangeColumnSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@ChangeColumnSecurity
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Data
public class UpdateColumnSecDto
{
    @NotNull
    Long columnSecId;

    @NotNull
    SecurityLevel securityLvl;
}
