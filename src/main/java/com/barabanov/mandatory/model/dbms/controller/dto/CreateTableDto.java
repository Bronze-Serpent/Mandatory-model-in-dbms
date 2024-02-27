package com.barabanov.mandatory.model.dbms.controller.dto;

import com.barabanov.mandatory.model.dbms.dto.ColumnDesc;
import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;
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
