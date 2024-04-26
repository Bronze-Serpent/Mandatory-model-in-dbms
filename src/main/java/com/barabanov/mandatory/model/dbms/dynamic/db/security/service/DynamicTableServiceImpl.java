package com.barabanov.mandatory.model.dbms.dynamic.db.security.service;

import com.barabanov.mandatory.model.dbms.dynamic.db.manager.DynamicDbManager;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.DbmsUser;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.exception.DbNotFoundException;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadTableSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.UserRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.AuthorityChecker;
import com.barabanov.mandatory.model.dbms.secure.sql.dto.ColumnDesc;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.ColumnSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.DatabaseSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.TableSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.exception.TableNotFoundException;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.ColumnSecurityRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.DbSecurityRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.TableSecurityRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.mapper.TableSecurityMapper;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.DynamicTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.Authority.ADMIN;
import static com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.Authority.USER;


@RequiredArgsConstructor
@Transactional
@Service
public class DynamicTableServiceImpl implements DynamicTableService
{
    private final DynamicDbManager dynamicDbManager;
    private final DbSecurityRepository dbSecurityRepository;
    private final TableSecurityRepository tableSecurityRepository;
    private final ColumnSecurityRepository columnSecurityRepository;
    private final TableSecurityMapper tableSecurityMapper;
    private final AuthorityChecker authorityChecker;
    private final SecretDataEraserImpl secretDataEraser;
    private final UserRepository userRepository;


    @Override
    public List<ReadTableSecDto> getListOfTablesInDb(Long dbSecId)
    {
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbSecId)
                .orElseThrow(() -> new DbNotFoundException(dbSecId, null));

        List<ReadTableSecDto> allTablesInDb = tableSecurityRepository.findAllInDb(dbSecId).stream()
                .map(tableSecurityMapper::toDto)
                .collect(Collectors.toList());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().contains(USER))
        {
            @SuppressWarnings("all") // поскольку при создании authentication такой же запрос в БД user не может не быть.
            SecurityLevel userSecLvl = userRepository.findSecurityLevelByLogin(authentication.getName()).get();
            return secretDataEraser.eraseTablesAccordingToSecurityLvl(allTablesInDb, userSecLvl);
        }
        else
        {
            if (authentication.getAuthorities().contains(ADMIN))
                authorityChecker.checkAdminLinkWithDb(authentication.getName(), dbSecurity);

            return allTablesInDb;
        }
    }


    @Override
    public ReadTableSecDto createTableInDb(Long dbId,
                                           String tableName,
                                           List<ColumnDesc> columnsDesc,
                                           SecurityLevel securityLevel)
    {
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId, null));

        authorityChecker.checkCurrentUserForChangeDb(dbSecurity);

        dynamicDbManager.createTable(dbSecurity.getName(), tableName, columnsDesc);
        TableSecurity tableSecurity = TableSecurity.builder()
                .name(tableName)
                .databaseSecurity(dbSecurity)
                .securityLevel(securityLevel)
                .build();
        createColumnSecurity(tableSecurity, columnsDesc);

        return tableSecurityMapper.toDto(tableSecurityRepository.save(tableSecurity));
    }


    @Override
    public ReadTableSecDto createTableInDb(Long dbId,
                                           String tableName,
                                           List<ColumnDesc> columnsDesc)
    {
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId, null));

        authorityChecker.checkCurrentUserForChangeDb(dbSecurity);

        dynamicDbManager.createTable(dbSecurity.getName(), tableName, columnsDesc);
        TableSecurity tableSecurity = TableSecurity.builder()
                .name(tableName)
                .databaseSecurity(dbSecurity)
                .securityLevel(dbSecurity.getSecurityLevel())
                .build();
        createColumnSecurity(tableSecurity, columnsDesc);

        return tableSecurityMapper.toDto(tableSecurityRepository.save(tableSecurity));
    }


    @Override
    public ReadTableSecDto changeTableSecLvl(Long tableId, SecurityLevel newSecLevel)
    {
        TableSecurity tableSecurity = tableSecurityRepository.findById(tableId)
                .orElseThrow(() -> new TableNotFoundException(tableId, null));

        authorityChecker.checkCurrentUserForChangeTable(tableSecurity);

        tableSecurity.setSecurityLevel(newSecLevel);

        return tableSecurityMapper.toDto(tableSecurityRepository.save(tableSecurity));
    }


    @Override
    public void dropTableInDb(Long tableId)
    {
        TableSecurity tableSecurity = tableSecurityRepository.findById(tableId)
                .orElseThrow(() -> new TableNotFoundException(tableId, null));

        authorityChecker.checkCurrentUserForChangeTable(tableSecurity);

        dynamicDbManager.dropTable(tableSecurity.getDatabaseSecurity().getName(), tableSecurity.getName());

        tableSecurityRepository.delete(tableSecurity);
    }


    private void createColumnSecurity(TableSecurity tableSecurity, List<ColumnDesc> columnsDesc)
    {
        for (ColumnDesc columnDesc : columnsDesc)
        {
            ColumnSecurity columnSecurity = new ColumnSecurity();
            columnSecurity.setName(columnDesc.getName());
            columnSecurity.setTableSecurity(tableSecurity);
            if (columnDesc.getSecurityLevel() == null)
                columnSecurity.setSecurityLevel(tableSecurity.getSecurityLevel());
            else
                columnSecurity.setSecurityLevel(columnDesc.getSecurityLevel());

            columnSecurityRepository.save(columnSecurity);
        }
    }

}
