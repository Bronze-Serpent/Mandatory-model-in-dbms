package com.barabanov.mandatory.model.dbms.dto;

import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ValueSecurityInfo
{
    String columnName;
    SecurityLevel securityLevel;
}
