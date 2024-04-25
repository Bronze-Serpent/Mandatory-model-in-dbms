package com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import org.springframework.jdbc.support.rowset.SqlRowSet;


public interface SecretDataEraser
{
    String eraseDataAccordingToSecurityLvl(Long tableSecId, SqlRowSet rowSet, SecurityLevel securityLevel);
}
