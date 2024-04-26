package com.barabanov.mandatory.model.dbms.controller.rest.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ExecuteSelectDto
{
    String selectSql;
    Long tableId;
}
