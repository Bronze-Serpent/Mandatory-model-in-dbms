package com.barabanov.mandatory.model.dbms.service;

import com.barabanov.mandatory.model.dbms.entity.DatabaseSecurity;
import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.exception.DbNotFoundException;
import com.barabanov.mandatory.model.dbms.repository.DbManager;
import com.barabanov.mandatory.model.dbms.repository.DbSecurityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
public class DbService
{
    private final DbManager dbManager;
    private final DbSecurityRepository dbSecurityRepository;


    public void createDb(String dbName, SecurityLevel securityLevel)
    {
        dbManager.createDb(dbName);
        DatabaseSecurity createdDbSecurity = DatabaseSecurity.builder()
                .name(dbName)
                .securityLevel(securityLevel)
                .build();

        dbSecurityRepository.save(createdDbSecurity);
    }


    public void changeDbSecLvl(Long dbId, SecurityLevel securityLevel)
    {
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId));
        dbSecurityRepository.flush();
    }


    public void deleteDb(Long dbId)
    {
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId));

        dbManager.dropDb(dbSecurity.getName());
        dbSecurityRepository.delete(dbSecurity);
    }
}
