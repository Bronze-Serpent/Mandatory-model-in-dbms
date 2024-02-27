package com.barabanov.mandatory.model.dbms.dynamic.db.security.dto;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Builder
@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReadDbSecDto
{
    Long secId;
    String name;
    SecurityLevel securityLevel;
    Long ownerAdminId;
    List<Long> adminsId;
}
