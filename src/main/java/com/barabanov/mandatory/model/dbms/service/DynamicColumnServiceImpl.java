package com.barabanov.mandatory.model.dbms.service;

import com.barabanov.mandatory.model.dbms.service.dto.ReadColumnSecDto;
import com.barabanov.mandatory.model.dbms.entity.ColumnSecurity;
import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.exception.ColumnNotFoundException;
import com.barabanov.mandatory.model.dbms.database.ColumnSecurityRepository;
import com.barabanov.mandatory.model.dbms.mapper.ColumnSecurityMapper;
import com.barabanov.mandatory.model.dbms.service.iterface.DynamicColumnService;
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


    @Override
    public ReadColumnSecDto changeColumnSecLvl(Long columnId, SecurityLevel newSecLvl)
    {
        ColumnSecurity columnSecurity = columnSecurityRepository.findById(columnId)
                .orElseThrow(() -> new ColumnNotFoundException(columnId, null));

        columnSecurity.setSecurityLevel(newSecLvl);

        return columnSecurityMapper.toDto(columnSecurityRepository.save(columnSecurity));
    }
}
