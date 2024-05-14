package com.barabanov.mandatory.model.dbms.dynamic.db.security.service;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadValueSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.ColumnSecurityRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.ValueSecurityRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.ColumnSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.ValueSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.exception.ColumnNotFoundException;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.mapper.ValueSecurityMapper;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.AuthorityChecker;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.DynamicValService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
public class DynamicValServiceImpl implements DynamicValService
{
    private final ColumnSecurityRepository columnSecurityRepository;
    private final ValueSecurityRepository valueSecurityRepository;
    private final ValueSecurityMapper valueSecurityMapper;
    private final AuthorityChecker authorityChecker;


//    TODO: вообще можно хотя бы sequence проверять для этой БД, чтобы хотя бы несуществующим tuple не изменять уровень секретности.
    @Override
    public ReadValueSecDto changeValueSecLvl(Long tupleId,
                                             Long columnId,
                                             SecurityLevel newSecLvl)
    {
        ColumnSecurity columnSecurity = columnSecurityRepository.findById(columnId)
                .orElseThrow(() -> new ColumnNotFoundException(columnId, null));

        authorityChecker.checkCurrentUserForValueAccess(columnSecurity);

        ValueSecurity currValueSecurity = valueSecurityRepository.findByTupleAndColumnId(tupleId, columnId)
                .orElseGet(() -> ValueSecurity.builder()
                        .tupleId(tupleId)
                        .columnSecurity(columnSecurity)
                        .build()
                );
        currValueSecurity.setSecurityLevel(newSecLvl);

        return valueSecurityMapper.toDto(valueSecurityRepository.save(currValueSecurity));
    }

}
