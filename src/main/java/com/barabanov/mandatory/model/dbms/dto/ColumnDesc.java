package com.barabanov.mandatory.model.dbms.dto;

import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;
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
