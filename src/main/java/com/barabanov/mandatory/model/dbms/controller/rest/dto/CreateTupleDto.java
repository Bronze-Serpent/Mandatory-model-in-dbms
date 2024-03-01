package com.barabanov.mandatory.model.dbms.controller.rest.dto;

import com.barabanov.mandatory.model.dbms.controller.rest.validation.ValidSecureSql;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Data
@Builder
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CreateTupleDto implements SecureSqlContainer
{
    @NotNull
    Long dbSecId;

    @NotEmpty
    @ValidSecureSql
    String secureSql;
}
