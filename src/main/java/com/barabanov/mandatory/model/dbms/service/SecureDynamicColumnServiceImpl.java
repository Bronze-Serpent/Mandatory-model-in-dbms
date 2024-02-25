package com.barabanov.mandatory.model.dbms.service;

import com.barabanov.mandatory.model.dbms.entity.ColumnSecurity;
import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.exception.ColumnNotFoundException;
import com.barabanov.mandatory.model.dbms.database.ColumnSecurityRepository;
import com.barabanov.mandatory.model.dbms.service.iterface.SecureDynamicColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional
@Service
public class SecureDynamicColumnServiceImpl implements SecureDynamicColumnService
{
    private final ColumnSecurityRepository columnSecurityRepository;


    @Override
    public void changeColumnSecLvl(Long columnId, SecurityLevel newSecLvl)
    {
        ColumnSecurity columnSecurity = columnSecurityRepository.findById(columnId)
                .orElseThrow(() -> new ColumnNotFoundException(columnId, null));

        columnSecurity.setSecurityLevel(newSecLvl);
        columnSecurityRepository.flush();
    }
}
