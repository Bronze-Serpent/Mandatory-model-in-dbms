package com.barabanov.mandatory.model.dbms.controller.dto;

import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Builder
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Data
public class ReadTupleSecurityDto
{
    Long tupleId;
    SecurityLevel securityLevel;
    Long tableSecId;
}
