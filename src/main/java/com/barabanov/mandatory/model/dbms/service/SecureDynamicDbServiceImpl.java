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


@RequiredArgsConstructor
@Transactional
@Service
public class SecureDynamicDbServiceImpl implements SecureDynamicDbService
{
    private final DynamicDbManager dynamicDbManager;
    private final DbSecurityRepository dbSecurityRepository;


    @Override
    public void createDb(String dbName, SecurityLevel securityLevel)
    {
        dynamicDbManager.createDb(dbName);
        DatabaseSecurity createdDbSecurity = DatabaseSecurity.builder()
                .name(dbName)
                .securityLevel(securityLevel)
                .build();

        dbSecurityRepository.save(createdDbSecurity);
    }


    @Override
    public void changeDbSecLvl(Long dbId, SecurityLevel newSecLevel)
    {
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId));

        dbSecurity.setSecurityLevel(newSecLevel);
        dbSecurityRepository.flush();
    }


    @Override
    public void deleteDb(Long dbId)
    {
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId));

        dynamicDbManager.dropDb(dbSecurity.getName());
        dbSecurityRepository.delete(dbSecurity);
    }
}
