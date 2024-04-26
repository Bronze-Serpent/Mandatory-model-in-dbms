package com.barabanov.mandatory.model.dbms.dynamic.db.security.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;


@SuperBuilder
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReadDbSecAdminDto extends ReadDbSecDto
{
    Long ownerAdminId;
    List<Long> adminsId;
}
