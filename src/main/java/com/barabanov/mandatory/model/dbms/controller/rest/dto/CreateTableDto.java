package com.barabanov.mandatory.model.dbms.controller.rest.dto;

import com.barabanov.mandatory.model.dbms.controller.rest.validation.AvailableTableName;
import com.barabanov.mandatory.model.dbms.controller.rest.validation.ColumnNotLowerSecurity;
import com.barabanov.mandatory.model.dbms.controller.rest.validation.TableNotLowerSecurity;
import com.barabanov.mandatory.model.dbms.secure.sql.dto.ColumnDesc;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@AvailableTableName
@ColumnNotLowerSecurity
@TableNotLowerSecurity
@Data
@Builder
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CreateTableDto
{
    @NotNull
    Long dbSecId;

    @NotEmpty
    String tableName;

    List<ColumnDesc> columnsDesc;

    SecurityLevel securityLevel;
}
