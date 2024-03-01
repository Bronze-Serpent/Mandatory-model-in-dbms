package com.barabanov.mandatory.model.dbms.controller.rest.validation.impl;

import com.barabanov.mandatory.model.dbms.controller.rest.dto.CreateTableDto;
import com.barabanov.mandatory.model.dbms.controller.rest.validation.TableNotLowerSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.DatabaseSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.DbSecurityRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Optional;


@RequiredArgsConstructor
public class TableSecurityValidator implements ConstraintValidator<TableNotLowerSecurity, CreateTableDto>
{
    private final DbSecurityRepository dbSecurityRepository;


    @Override
    public boolean isValid(CreateTableDto value, ConstraintValidatorContext context)
    {
        if (value.getSecurityLevel() == null)
            return true;

        Optional<DatabaseSecurity> databaseSecurity = dbSecurityRepository.findById(value.getDbSecId());
        return databaseSecurity.filter(dbSec -> dbSec
                        .getSecurityLevel()
                        .getImportantLvl() <= value.getSecurityLevel().getImportantLvl())
                .isPresent();
    }
}
