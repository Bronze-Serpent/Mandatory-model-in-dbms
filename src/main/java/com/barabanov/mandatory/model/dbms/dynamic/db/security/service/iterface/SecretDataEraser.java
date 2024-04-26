package com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadColumnSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadDbSecAdminDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadDbSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadTableSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.DbmsAdmin;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.List;


public interface SecretDataEraser
{
    String eraseRowSetAccordingToSecurityLvl(Long tableSecId, SqlRowSet rowSet, SecurityLevel securityLevel);

    List<ReadColumnSecDto> eraseColumnsAccordingToSecurityLvl(List<ReadColumnSecDto> columnDtos, SecurityLevel securityLevel);

    List<ReadTableSecDto> eraseTablesAccordingToSecurityLvl(List<ReadTableSecDto> tableDtos, SecurityLevel securityLevel);

    List<ReadDbSecDto> eraseDatabasesAccordingToSecurityLvl(List<ReadDbSecDto> databaseDtos, SecurityLevel securityLevel);

    List<ReadDbSecAdminDto> eraseDatabasesAccordingToAdminLinks(List<ReadDbSecAdminDto> allDatabases, DbmsAdmin dbmsAdmin);
}
