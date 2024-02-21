package com.barabanov.mandatory.model.dbms.dto;

import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Value
@SuperBuilder
public class ColumnDesc
{
    String name;
    String type;
    List<String> constraints;
    SecurityLevel securityLevel;
}
