package com.barabanov.mandatory.model.dbms.dynamic.db.security.service;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadColumnSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.ColumnSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.exception.ColumnNotFoundException;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.ColumnSecurityRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.mapper.ColumnSecurityMapper;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.AuthorityChecker;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.DynamicColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
public class DynamicColumnServiceImpl implements DynamicColumnService
{
    private final ColumnSecurityRepository columnSecurityRepository;
    private final ColumnSecurityMapper columnSecurityMapper;
    private final AuthorityChecker authorityChecker;


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
