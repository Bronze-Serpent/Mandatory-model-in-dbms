package com.barabanov.mandatory.model.dbms.controller.rest.dto;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CreateDbDto
{
    String name;
    SecurityLevel securityLevel;
}
