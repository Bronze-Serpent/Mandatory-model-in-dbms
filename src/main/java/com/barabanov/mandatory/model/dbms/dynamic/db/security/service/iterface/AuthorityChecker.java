package com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.ColumnSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.DatabaseSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.TableSecurity;


// TODO: подумать над тем, чтобы сделать проверку по всем ролям, а не только тем, что могут сюда добраться.
//  Да и вообще отрефакторить модель. Продумать её ещё раз.
public interface AuthorityChecker
{
    void checkCurrentUserForChangeDb(DatabaseSecurity dbSecurity);

    void checkCurrentUserForChangeTable(TableSecurity tableSecurity);

    void checkCurrentUserForChangeColumn(ColumnSecurity columnSecurity);

    void checkCurrentUserForTupleAccess(TableSecurity tableSecurity);

    void checkCurrentUserForValueAccess(ColumnSecurity columnSecurity);

    void checkCurrentUserForSelectOperation(Long dbSecId, String sqlSelect);

    void checkAdminLinkWithDb(String login, DatabaseSecurity dbSecurity);

    void checkAdminLinkWithDb(String login, TableSecurity tableSecurity);
}
