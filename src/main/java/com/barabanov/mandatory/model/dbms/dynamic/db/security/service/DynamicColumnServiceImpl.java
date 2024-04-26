package com.barabanov.mandatory.model.dbms.dynamic.db.security.service;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadColumnSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.ColumnSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.TableSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.DbmsUser;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.exception.ColumnNotFoundException;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.exception.TableNotFoundException;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.ColumnSecurityRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.mapper.ColumnSecurityMapper;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.TableSecurityRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.UserRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.AuthorityChecker;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.DynamicColumnService;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.SecretDataEraser;
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
public class DynamicColumnServiceImpl implements DynamicColumnService
{
    private final ColumnSecurityRepository columnSecurityRepository;
    private final TableSecurityRepository tableSecurityRepository;
    private final ColumnSecurityMapper columnSecurityMapper;
    private final AuthorityChecker authorityChecker;
    private final UserRepository userRepository;
    private final SecretDataEraser secretDataEraser;


    public List<ReadColumnSecDto> getListOfColumnsInTable(Long tableSecId)
    {
        TableSecurity tableSecurity = tableSecurityRepository.findById(tableSecId)
                .orElseThrow(() -> new TableNotFoundException(tableSecId, null));

        List<ReadColumnSecDto> allColumnsInTable = columnSecurityRepository.findAllInTable(tableSecId)
                .stream()
                .map(columnSecurityMapper::toDto)
                .collect(Collectors.toList());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().contains(USER))
        {
            @SuppressWarnings("all") // поскольку при создании authentication такой же запрос в БД user не может не быть.
            SecurityLevel userSecLvl = userRepository.findSecurityLevelByLogin(authentication.getName()).get();
            return secretDataEraser.eraseColumnsAccordingToSecurityLvl(allColumnsInTable, userSecLvl);
        }
        else
        {
            if (authentication.getAuthorities().contains(ADMIN))
                authorityChecker.checkAdminLinkWithDb(authentication.getName(), tableSecurity);

            return allColumnsInTable;
        }
    }


    @Override
    public ReadColumnSecDto changeColumnSecLvl(Long columnId, SecurityLevel newSecLvl)
    {
        ColumnSecurity columnSecurity = columnSecurityRepository.findById(columnId)
                .orElseThrow(() -> new ColumnNotFoundException(columnId, null));

        authorityChecker.checkCurrentUserForChangeColumn(columnSecurity);

        columnSecurity.setSecurityLevel(newSecLvl);

        return columnSecurityMapper.toDto(columnSecurityRepository.save(columnSecurity));
    }
}
