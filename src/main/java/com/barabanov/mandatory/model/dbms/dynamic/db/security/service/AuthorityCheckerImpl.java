package com.barabanov.mandatory.model.dbms.dynamic.db.security.service;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.ColumnSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.DatabaseSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.TableSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.DbmsAdmin;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.exception.CanNotChangeDbSchemaException;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.exception.ColumnNotFoundException;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.exception.DbNotFoundException;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.exception.TableNotFoundException;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.DbSecurityRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.UserRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.AuthorityChecker;
import com.barabanov.mandatory.model.dbms.sql.ParsedSqlDto;
import com.barabanov.mandatory.model.dbms.sql.SqlParser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.Authority.ADMIN;
import static com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.Authority.USER;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthorityCheckerImpl implements AuthorityChecker {
    private final UserRepository userRepository;
    private final DbSecurityRepository dbSecurityRepository;
    private final SqlParser sqlParser;


    @Override
    public void checkCurrentUserForSelectOperation(Long dbSecId, String sqlSelect) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        @SuppressWarnings("all") // поскольку user получается из БД при формировании authentication его не может не быть
        int userImportantLvl = userRepository.findSecurityLevelByLogin(authentication.getName())
                .get()
                .getImportantLvl();

        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbSecId)
                .orElseThrow(() -> new DbNotFoundException(dbSecId, null));

        if (authentication.getAuthorities().contains(USER)) {
            if (dbSecurity.getSecurityLevel().getImportantLvl() > userImportantLvl)
                throw new DbNotFoundException(dbSecId, null);

            ParsedSqlDto parsedSqlDto = sqlParser.parseSelectQuery(sqlSelect);
            List<TableSecurity> usedTables = dbSecurity.getTables()
                    .stream()
                    .filter(tableSec -> parsedSqlDto.getParsedFromSentence().contains(tableSec.getName()))
                    .toList();
            usedTables.forEach(tableSec -> {
                if (tableSec.getSecurityLevel().getImportantLvl() > userImportantLvl)
                    throw new TableNotFoundException(null, tableSec.getName());
            });

            usedTables.stream()
                    .flatMap(tableSecurity -> tableSecurity.getColumnSecurities().stream())
                    .filter(columnSecurity -> parsedSqlDto.getParsedSelectSentence().contains(columnSecurity.getName()))
                    .forEach(columnSec -> {
                        if (columnSec.getSecurityLevel().getImportantLvl() > userImportantLvl)
                            throw new ColumnNotFoundException(null, columnSec.getName());
                    });
        } else if (authentication.getAuthorities().contains(ADMIN))
            checkAdminLinkWithDb(authentication.getName(), dbSecurity);
    }


    @Override
    public void checkCurrentUserForChangeDb(DatabaseSecurity dbSecurity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().contains(ADMIN)) {
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
    public void checkCurrentUserForChangeTable(TableSecurity tableSecurity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().contains(ADMIN)) {
            String login = authentication.getName();
            DatabaseSecurity dbSecurity = tableSecurity.getDatabaseSecurity();

            checkAdminLinkWithDb(authentication.getName(), tableSecurity);

            if (!dbSecurity.getOwner().getLogin().equals(login))
                throw new CanNotChangeDbSchemaException(dbSecurity.getId(),
                        "Изменять схему базы данных может только её владелец или супер пользователь");
        }
    }


    @Override
    public void checkCurrentUserForChangeColumn(ColumnSecurity columnSecurity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().contains(ADMIN)) {
            String login = authentication.getName();
            DatabaseSecurity dbSecurity = columnSecurity.getTableSecurity().getDatabaseSecurity();

            checkAdminLinkWithDb(authentication.getName(), columnSecurity);

            if (!dbSecurity.getOwner().getLogin().equals(login))
                throw new CanNotChangeDbSchemaException(dbSecurity.getId(),
                        "Изменять схему базы данных может только её владелец или супер пользователь");
        }
    }


    @Override
    public void checkCurrentUserForTupleAccess(TableSecurity tableSecurity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().contains(ADMIN))
            checkAdminLinkWithDb(authentication.getName(), tableSecurity);
    }


    @Override
    public void checkCurrentUserForValueAccess(ColumnSecurity columnSecurity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().contains(ADMIN))
            checkAdminLinkWithDb(authentication.getName(), columnSecurity);
    }


    //TODO: checkAdminLinkWithDb можно переписать как 1 обобщённый метод.
    private void checkAdminLinkWithDb(String login, ColumnSecurity columnSecurity) {
        DatabaseSecurity dbSecurity = columnSecurity.getTableSecurity().getDatabaseSecurity();

        if (!(dbSecurity.getOwner().getLogin().equals(login) ||
                dbSecurity.getAdmins().stream()
                        .map(DbmsAdmin::getLogin)
                        .anyMatch(adminLogin -> adminLogin.equals(login))))
            throw new ColumnNotFoundException(columnSecurity.getId(), null);
    }


    @Override
    public void checkAdminLinkWithDb(String login, TableSecurity tableSecurity) {
        DatabaseSecurity dbSecurity = tableSecurity.getDatabaseSecurity();

        if (!(dbSecurity.getOwner().getLogin().equals(login) ||
                dbSecurity.getAdmins().stream()
                        .map(DbmsAdmin::getLogin)
                        .anyMatch(adminLogin -> adminLogin.equals(login))))
            throw new TableNotFoundException(tableSecurity.getId(), null);
    }


    @Override
    public void checkAdminLinkWithDb(String login, DatabaseSecurity dbSecurity) {
        if (!(dbSecurity.getOwner().getLogin().equals(login) ||
                dbSecurity.getAdmins().stream()
                        .map(DbmsAdmin::getLogin)
                        .anyMatch(adminLogin -> adminLogin.equals(login))))
            throw new DbNotFoundException(dbSecurity.getId(), null);
    }

}
