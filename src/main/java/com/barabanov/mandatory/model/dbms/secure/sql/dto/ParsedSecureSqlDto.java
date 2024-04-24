package com.barabanov.mandatory.model.dbms.secure.sql.dto;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ParsedSecureSqlDto
{
    String tableName;
    String sql;
    List<ValueSecurityInfo> valueSecurityInfoList;
    SecurityLevel rowSecurityLvl;
}
