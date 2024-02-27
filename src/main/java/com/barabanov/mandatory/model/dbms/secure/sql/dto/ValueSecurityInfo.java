package com.barabanov.mandatory.model.dbms.secure.sql.dto;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ValueSecurityInfo
{
    String columnName;
    SecurityLevel securityLevel;
}
