package com.barabanov.mandatory.model.dbms.dto;

import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;


@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ValueSecurityInfo
{
    String columnName;
    SecurityLevel securityLevel;
}
