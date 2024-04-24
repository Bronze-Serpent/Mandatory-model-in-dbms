package com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.ColumnSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.DatabaseSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.TableSecurity;


public interface AuthorityChecker
{
    void checkCurrentUserForChangeDb(DatabaseSecurity dbSecurity);

    void checkCurrentUserForChangeTable(TableSecurity tableSecurity);

    void checkCurrentUserForChangeColumn(ColumnSecurity columnSecurity);

    void checkCurrentUserForTupleAccess(TableSecurity tableSecurity);

    void checkCurrentUserForValueAccess(ColumnSecurity columnSecurity);
}
