package com.barabanov.mandatory.model.dbms.service;

import com.barabanov.mandatory.model.dbms.entity.DatabaseSecurity;
import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.exception.DbNotFoundException;
import com.barabanov.mandatory.model.dbms.database.DbSecurityRepository;
import com.barabanov.mandatory.model.dbms.database.DynamicDbManager;
import com.barabanov.mandatory.model.dbms.service.iterface.SecureDynamicDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;


@RequiredArgsConstructor
@Service
public class SecureDynamicDbServiceImpl implements SecureDynamicDbService
{
    private final DynamicDbManager dynamicDbManager;
    private final DbSecurityRepository dbSecurityRepository;
    private final TransactionTemplate transactionTemplate;


    @Override
    public void createDb(String dbName, SecurityLevel securityLevel)
    {
        dynamicDbManager.createDb(dbName);
        transactionTemplate.executeWithoutResult(transaction ->
        {
            DatabaseSecurity createdDbSecurity = DatabaseSecurity.builder()
                    .name(dbName)
                    .securityLevel(securityLevel)
                    .build();

            dbSecurityRepository.save(createdDbSecurity);
        });
    }


    @Override
    @Transactional
    public void changeDbSecLvl(Long dbId, SecurityLevel newSecLevel)
    {

        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId, null));

        dbSecurity.setSecurityLevel(newSecLevel);
    }


    @Override
    public void deleteDb(Long dbId)
    {
        // TODO: 25.02.2024 тут нужна транзакция?
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId, null));

        dynamicDbManager.dropDb(dbSecurity.getName());
        transactionTemplate.executeWithoutResult(transaction -> dbSecurityRepository.delete(dbSecurity));
    }
}
