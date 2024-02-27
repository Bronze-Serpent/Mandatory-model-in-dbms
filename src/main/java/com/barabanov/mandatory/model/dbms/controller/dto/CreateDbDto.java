package com.barabanov.mandatory.model.dbms.controller.dto;

import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;
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
