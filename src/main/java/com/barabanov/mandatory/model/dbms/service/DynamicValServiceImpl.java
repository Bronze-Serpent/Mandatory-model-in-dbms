package com.barabanov.mandatory.model.dbms.service;

import com.barabanov.mandatory.model.dbms.controller.dto.ReadValueSecDto;
import com.barabanov.mandatory.model.dbms.database.ColumnSecurityRepository;
import com.barabanov.mandatory.model.dbms.database.ValueSecurityRepository;
import com.barabanov.mandatory.model.dbms.entity.ColumnSecurity;
import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.entity.ValueSecurity;
import com.barabanov.mandatory.model.dbms.exception.ColumnNotFoundException;
import com.barabanov.mandatory.model.dbms.mapper.ValueSecurityMapper;
import com.barabanov.mandatory.model.dbms.service.iterface.DynamicValService;
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


    @Override
    public ReadValueSecDto changeValueSecLvl(Long tupleId,
                                             Long columnId,
                                             SecurityLevel newSecLvl)
    {
        ColumnSecurity columnSecurity = columnSecurityRepository.findById(columnId)
                .orElseThrow(() -> new ColumnNotFoundException(columnId, null));

        ValueSecurity currValueSecurity = valueSecurityRepository.findByTupleIdAndColumnInTable(tupleId, columnId)
                .orElseGet(() -> ValueSecurity.builder()
                        .tupleId(tupleId)
                        .columnSecurity(columnSecurity)
                        .build()
                );
        currValueSecurity.setSecurityLevel(newSecLvl);

        return valueSecurityMapper.toDto(valueSecurityRepository.save(currValueSecurity));
    }
}
