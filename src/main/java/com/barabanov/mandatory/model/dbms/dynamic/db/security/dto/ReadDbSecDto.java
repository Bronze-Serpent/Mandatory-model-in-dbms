package com.barabanov.mandatory.model.dbms.dynamic.db.security.dto;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;


@Data
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReadDbSecDto
{
    Long secId;
    String name;
    SecurityLevel securityLevel;
}
