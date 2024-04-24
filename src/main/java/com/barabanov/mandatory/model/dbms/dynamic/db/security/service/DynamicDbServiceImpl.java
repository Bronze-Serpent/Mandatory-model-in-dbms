package com.barabanov.mandatory.model.dbms.dynamic.db.security.service;

import com.barabanov.mandatory.model.dbms.dynamic.db.manager.DynamicDbManager;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadDbSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.DatabaseSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.DbmsAdmin;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.exception.DbNotFoundException;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.mapper.DatabaseSecurityMapper;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.AdminRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.DbSecurityRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.AuthorityChecker;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.DynamicDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;


@RequiredArgsConstructor
@Service
public class DynamicDbServiceImpl implements DynamicDbService
{
    private final DynamicDbManager dynamicDbManager;
    private final DbSecurityRepository dbSecurityRepository;
    private final TransactionTemplate transactionTemplate;
    private final DatabaseSecurityMapper dbSecurityMapper;
    private final AdminRepository adminRepository;
    private final AuthorityChecker authorityChecker;


    @Override
    public ReadDbSecDto createDb(String dbName, SecurityLevel securityLevel)
    {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        @SuppressWarnings("all")
        DbmsAdmin dbmsAdmin = adminRepository.findByLogin(login).get();

        dynamicDbManager.createDb(dbName);
        return transactionTemplate.execute(transaction ->
        {
            DatabaseSecurity createdDbSecurity = DatabaseSecurity.builder()
                    .name(dbName)
                    .owner(dbmsAdmin)
                    .securityLevel(securityLevel)
                    .build();

            return dbSecurityMapper.toDto(dbSecurityRepository.save(createdDbSecurity));
        });
    }


    @Override
    @Transactional
    public ReadDbSecDto changeDbSecLvl(Long dbId, SecurityLevel newSecLevel)
    {
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId, null));

        authorityChecker.checkCurrentUserForChangeDb(dbSecurity);

        dbSecurity.setSecurityLevel(newSecLevel);
        return dbSecurityMapper.toDto(dbSecurity);
    }


    @Override
    public void deleteDb(Long dbId)
    {
        // TODO: 25.02.2024 тут нужна транзакция?
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId, null));

        authorityChecker.checkCurrentUserForChangeDb(dbSecurity);

        dynamicDbManager.dropDb(dbSecurity.getName());
        transactionTemplate.executeWithoutResult(transaction -> dbSecurityRepository.delete(dbSecurity));
    }

}
