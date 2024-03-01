package com.barabanov.mandatory.model.dbms.controller.rest.validation.impl;

import com.barabanov.mandatory.model.dbms.controller.rest.dto.UpdateTableSecDto;
import com.barabanov.mandatory.model.dbms.controller.rest.validation.ChangeTableSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.DatabaseSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.TableSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.DbSecurityRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.TableSecurityRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Optional;


@RequiredArgsConstructor
public class ChangeTableSecurityValidator implements ConstraintValidator<ChangeTableSecurity, UpdateTableSecDto>
{
    private final TableSecurityRepository tableSecurityRepository;


    @Override
    public boolean isValid(UpdateTableSecDto value, ConstraintValidatorContext context)
    {
        if (value.getSecurityLevel() == null)
            return false;

        Optional<TableSecurity> tableSecOptional = tableSecurityRepository.findById(value.getTableSecId());

        return tableSecOptional
                .filter(tableSec ->
                        value.getSecurityLevel().getImportantLvl() >=
                                tableSec.getDatabaseSecurity().getSecurityLevel().getImportantLvl())
                .isPresent();
    }
}
