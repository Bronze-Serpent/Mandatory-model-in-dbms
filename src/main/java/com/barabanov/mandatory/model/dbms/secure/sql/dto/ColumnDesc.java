package com.barabanov.mandatory.model.dbms.secure.sql.dto;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Builder
@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ColumnDesc
{
    String name;
    String type;
    List<String> constraints;
    SecurityLevel securityLevel;
}
