package com.barabanov.mandatory.model.dbms.dynamic.db.security.service;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.ColumnSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.DatabaseSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.TableSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.DbmsAdmin;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.exception.CanNotChangeDbSchemaException;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.exception.ColumnNotFoundException;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.exception.DbNotFoundException;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.exception.TableNotFoundException;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.AuthorityChecker;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.Authority.ADMIN;


@Service
@Transactional(readOnly = true)
public class AuthorityCheckerImpl implements AuthorityChecker
{

    @Override
    public void checkCurrentUserForChangeDb(DatabaseSecurity dbSecurity)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().contains(ADMIN))
        {
            String login = authentication.getName();

            if (!(dbSecurity.getOwner().getLogin().equals(login) ||
                    dbSecurity.getAdmins().stream()
                            .map(DbmsAdmin::getLogin)
                            .anyMatch(adminLogin -> adminLogin.equals(login))))
                throw new DbNotFoundException(dbSecurity.getId(), null);

            if (!dbSecurity.getOwner().getLogin().equals(login))
                throw new CanNotChangeDbSchemaException(dbSecurity.getId(),
                        "Изменять схему базы данных может только её владелец или супер пользователь");
        }
    }


    @Override
    public void checkCurrentUserForChangeTable(TableSecurity tableSecurity)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().contains(ADMIN))
        {
            String login = authentication.getName();
            DatabaseSecurity dbSecurity = tableSecurity.getDatabaseSecurity();

            checkAdminLinkWithDb(authentication, tableSecurity);

            if (!dbSecurity.getOwner().getLogin().equals(login))
                throw new CanNotChangeDbSchemaException(dbSecurity.getId(),
                        "Изменять схему базы данных может только её владелец или супер пользователь");
        }
    }


    @Override
    public void checkCurrentUserForChangeColumn(ColumnSecurity columnSecurity)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().contains(ADMIN))
        {
            String login = authentication.getName();
            DatabaseSecurity dbSecurity = columnSecurity.getTableSecurity().getDatabaseSecurity();

            checkAdminLinkWithDb(authentication, columnSecurity);

            if (!dbSecurity.getOwner().getLogin().equals(login))
                throw new CanNotChangeDbSchemaException(dbSecurity.getId(),
                        "Изменять схему базы данных может только её владелец или супер пользователь");
        }
    }


    @Override
    public void checkCurrentUserForTupleAccess(TableSecurity tableSecurity)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().contains(ADMIN))
            checkAdminLinkWithDb(authentication, tableSecurity);
    }


    @Override
    public void checkCurrentUserForValueAccess(ColumnSecurity columnSecurity)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().contains(ADMIN))
            checkAdminLinkWithDb(authentication, columnSecurity);
    }


    private void checkAdminLinkWithDb(Authentication authentication, ColumnSecurity columnSecurity)
    {
        String login = authentication.getName();

        DatabaseSecurity dbSecurity = columnSecurity.getTableSecurity().getDatabaseSecurity();
        if (!(dbSecurity.getOwner().getLogin().equals(login) ||
                dbSecurity.getAdmins().stream()
                        .map(DbmsAdmin::getLogin)
                        .anyMatch(adminLogin -> adminLogin.equals(login))))
            throw new ColumnNotFoundException(columnSecurity.getId(), null);
    }


    private void checkAdminLinkWithDb(Authentication authentication, TableSecurity tableSecurity)
    {
        String login = authentication.getName();
        DatabaseSecurity dbSecurity = tableSecurity.getDatabaseSecurity();

        if (!(dbSecurity.getOwner().getLogin().equals(login) ||
                dbSecurity.getAdmins().stream()
                        .map(DbmsAdmin::getLogin)
                        .anyMatch(adminLogin -> adminLogin.equals(login))))
            throw new TableNotFoundException(tableSecurity.getId(), null);
    }
}
