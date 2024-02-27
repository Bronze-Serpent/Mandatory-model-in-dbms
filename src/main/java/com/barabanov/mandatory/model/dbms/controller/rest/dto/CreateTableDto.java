package com.barabanov.mandatory.model.dbms.controller.rest.dto;

import com.barabanov.mandatory.model.dbms.secure.sql.dto.ColumnDesc;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Data
@Builder
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CreateTableDto
{
    Long dbSecId;
    String tableName;
    List<ColumnDesc> columnsDesc;
    SecurityLevel securityLevel;
}
