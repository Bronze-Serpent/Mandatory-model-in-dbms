package com.barabanov.mandatory.model.dbms.controller.dto;

import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Data
@Builder
public class UpdateTupleSecurityDto
{
    Long tableSecId;
    Long tupleId;
    SecurityLevel newSecurityLvl;
}
