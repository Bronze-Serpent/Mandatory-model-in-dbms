package com.barabanov.mandatory.model.dbms.dynamic.db.security.service;

import com.barabanov.mandatory.model.dbms.dynamic.db.manager.DynamicDbManager;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.*;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.*;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.AuthorityChecker;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.DynamicTupleService;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.SecretDataEraser;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.SqlRowSetConverter;
import com.barabanov.mandatory.model.dbms.secure.sql.service.SecuritySqlParser;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadTupleSecurityDto;
import com.barabanov.mandatory.model.dbms.secure.sql.dto.ParsedSecureSqlDto;
import com.barabanov.mandatory.model.dbms.secure.sql.dto.ValueSecurityInfo;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.exception.ColumnNotFoundException;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.exception.ConversionRowSetException;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.exception.DbNotFoundException;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.exception.TableNotFoundException;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.mapper.TupleSecurityMapper;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import static com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.Authority.ADMIN;
import static com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.Authority.USER;


@RequiredArgsConstructor
@Transactional
@Service
public class DynamicTupleServiceImpl implements DynamicTupleService
{
    private final DynamicDbManager dynamicDbManager;
    private final DbSecurityRepository dbSecurityRepository;
    private final TableSecurityRepository tableSecurityRepository;
    private final ColumnSecurityRepository columnSecurityRepository;
    private final TupleSecurityRepository tupleSecurityRepository;
    private final ValueSecurityRepository valueSecurityRepository;
    private final UserRepository userRepository;
    private final SecuritySqlParser securitySqlParser;
    private final TupleSecurityMapper tupleSecurityMapper;
    private final AuthorityChecker authorityChecker;
    private final SecretDataEraser secretDataEraser;
    private final SqlRowSetConverter sqlRowSetConverter;



    @Override
    public String executeSelectInTable(Long tableSecId, String sqlSelect)
    {
        TableSecurity tableSecurity = tableSecurityRepository.findById(tableSecId)
                .orElseThrow(() -> new TableNotFoundException(tableSecId, null));
        Long dbId = tableSecurity.getDatabaseSecurity().getId();

        authorityChecker.checkCurrentUserForSelectOperation(dbId, sqlSelect);
        SqlRowSet sqlRowSet = executeSqlInDb(dbId, sqlSelect);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().contains(USER))
        {
            @SuppressWarnings("all") // поскольку при создании authentication такой же запрос в БД user не может не быть.
            SecurityLevel userSecLvl = userRepository.findSecurityLevelByLogin(authentication.getName()).get();
            return secretDataEraser.eraseRowSetAccordingToSecurityLvl(tableSecId, sqlRowSet, userSecLvl);
        }
        else
            if (authentication.getAuthorities().contains(ADMIN))
            {
                authorityChecker.checkAdminLinkWithDb(authentication.getName(), tableSecurity);
                return sqlRowSetConverter.convertToJson(sqlRowSet);
            }
        return null;
    }


    @Override
    public ReadTupleSecurityDto insertIntoDb(Long dbId, String securitySql)
    {
        ParsedSecureSqlDto parsedSecureSqlDto = securitySqlParser.parse(securitySql);

        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId, null));
        TableSecurity tableSecurity = tableSecurityRepository.findByNameInDb(dbSecurity.getId(), parsedSecureSqlDto.getTableName())
                .orElseThrow(() -> new TableNotFoundException(null, parsedSecureSqlDto.getTableName()));

        Long insertedTupleId = dynamicDbManager.insertTuple(dbSecurity.getName(), parsedSecureSqlDto.getSql());

        TupleSecurity tupleSecEntity = createSecurityRecords(
                parsedSecureSqlDto.getRowSecurityLvl(),
                insertedTupleId,
                tableSecurity,
                parsedSecureSqlDto.getValueSecurityInfoList()
        );

        return tupleSecurityMapper.toDto(tupleSecEntity);
    }


    @Override
    public ReadTupleSecurityDto changeTupleSecLvl(Long tableId,
                                                  Long tupleId,
                                                  SecurityLevel newSecurityLvl)
    {
        // TODO: 27.02.2024 можно сделать проверку, если новый уровень безопасности равен уровню безопасности таблицы, то просто удалять запись
        TableSecurity tableSecurity = tableSecurityRepository.findById(tableId)
                .orElseThrow(() -> new TableNotFoundException(tableId, null));

        authorityChecker.checkCurrentUserForTupleAccess(tableSecurity);

        TupleSecurity tupleSecurity = tupleSecurityRepository.findByTupleIdInTable(tableId, tupleId)
                .orElseGet(() -> TupleSecurity.builder()
                        .tupleId(tupleId)
                        .tableSecurity(tableSecurity)
                        .build());

        tupleSecurity.setSecurityLevel(newSecurityLvl);

        return tupleSecurityMapper.toDto(tupleSecurity);
    }


    @Override
    public void deleteTupleInTable(Long tableId, Long tupleId)
    {
        TableSecurity tableSecurity = tableSecurityRepository.findById(tableId)
                .orElseThrow(() -> new TableNotFoundException(tableId, null));

        authorityChecker.checkCurrentUserForTupleAccess(tableSecurity);

        dynamicDbManager.deleteTuple(
                tableSecurity.getDatabaseSecurity().getName(),
                tableSecurity.getName(),
                tupleId);

        tupleSecurityRepository.deleteByTupleId(tupleId);
    }


    private SqlRowSet executeSqlInDb(Long dbId, String sqlSelect)
    {
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId, null));

        return dynamicDbManager.executeSqlInDb(dbSecurity.getName(), sqlSelect);
    }


    private TupleSecurity createSecurityRecords(SecurityLevel rowSecurityLvl,
                                       Long insertedTupleId,
                                       TableSecurity tableSecurity,
                                       List<ValueSecurityInfo> valueSecurityInfoList)
    {
        TupleSecurity tupleSecurity = new TupleSecurity(insertedTupleId, tableSecurity, rowSecurityLvl);
        if (rowSecurityLvl != null)
            tupleSecurityRepository.save(tupleSecurity);

        for (ValueSecurityInfo valueSecurityInfo : valueSecurityInfoList)
        {
            ColumnSecurity columnSecurity = columnSecurityRepository.findByNameInTable(tableSecurity.getId(), valueSecurityInfo.getColumnName())
                    .orElseThrow(() -> new ColumnNotFoundException(null, valueSecurityInfo.getColumnName()));
            ValueSecurity valueSecurity = new ValueSecurity(
                    insertedTupleId,
                    columnSecurity,
                    valueSecurityInfo.getSecurityLevel()
            );
            valueSecurityRepository.save(valueSecurity);
        }

        return tupleSecurity;
    }

}