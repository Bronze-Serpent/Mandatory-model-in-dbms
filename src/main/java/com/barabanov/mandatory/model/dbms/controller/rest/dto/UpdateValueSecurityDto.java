package com.barabanov.mandatory.model.dbms.controller.rest.dto;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Builder
@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UpdateValueSecurityDto
{
    Long columnSecId;
    Long tupleId;
    SecurityLevel securityLevel;
}
