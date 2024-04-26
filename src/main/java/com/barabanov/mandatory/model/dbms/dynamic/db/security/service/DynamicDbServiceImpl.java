package com.barabanov.mandatory.model.dbms.dynamic.db.security.service;

import com.barabanov.mandatory.model.dbms.dynamic.db.manager.DynamicDbManager;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadDbSecAdminDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadDbSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.DatabaseSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.DbmsAdmin;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.DbmsUser;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.exception.DbNotFoundException;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.mapper.DatabaseSecurityMapper;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.AdminRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.DbSecurityRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.UserRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.AuthorityChecker;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.DynamicDbService;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.SecretDataEraser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import static com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.Authority.*;


@RequiredArgsConstructor
@Service
public class DynamicDbServiceImpl implements DynamicDbService
{
    private final DynamicDbManager dynamicDbManager;
    private final DbSecurityRepository dbSecurityRepository;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final TransactionTemplate transactionTemplate;
    private final DatabaseSecurityMapper dbSecurityMapper;
    private final AuthorityChecker authorityChecker;
    private final SecretDataEraser secretDataEraser;


    @Override
    public List<ReadDbSecDto> getDatabasesList()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().contains(USER))
        {
            List<ReadDbSecDto> allDatabases = dbSecurityRepository.findAll()
                    .stream()
                    .map(dbSecurityMapper::toDto)
                    .toList();
            @SuppressWarnings("all") // поскольку при создании authentication такой же запрос в БД user не может не быть.
            SecurityLevel userSecLvl = userRepository.findSecurityLevelByLogin(authentication.getName()).get();
            return secretDataEraser.eraseDatabasesAccordingToSecurityLvl(allDatabases, userSecLvl);
        }
        else
        {
            List<ReadDbSecAdminDto> allDatabases = dbSecurityRepository.findAll()
                    .stream()
                    .map(dbSecurityMapper::toAdminDto)
                    .toList();
            if (authentication.getAuthorities().contains(ADMIN))
            {
                @SuppressWarnings("all") // поскольку при создании authentication такой же запрос в БД admin не может не быть.
                DbmsAdmin dbmsAdmin = adminRepository.findByLogin(authentication.getName()).get();

                return secretDataEraser.eraseDatabasesAccordingToAdminLinks(allDatabases, dbmsAdmin).stream()
                        .map(dbAdminDto -> (ReadDbSecDto) dbAdminDto)
                        .toList();
            }
            else
                return allDatabases.stream()
                        .map(dbAdminDto -> (ReadDbSecDto) dbAdminDto)
                        .toList();
        }
    }


    @Override
    public ReadDbSecAdminDto createDb(String dbName, SecurityLevel securityLevel)
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

            return dbSecurityMapper.toAdminDto(dbSecurityRepository.save(createdDbSecurity));
        });
    }


    @Override
    @Transactional
    public ReadDbSecAdminDto changeDbSecLvl(Long dbId, SecurityLevel newSecLevel)
    {
        DatabaseSecurity dbSecurity = dbSecurityRepository.findById(dbId)
                .orElseThrow(() -> new DbNotFoundException(dbId, null));

        authorityChecker.checkCurrentUserForChangeDb(dbSecurity);

        dbSecurity.setSecurityLevel(newSecLevel);
        return dbSecurityMapper.toAdminDto(dbSecurity);
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
