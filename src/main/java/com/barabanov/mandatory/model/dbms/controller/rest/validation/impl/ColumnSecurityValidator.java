package com.barabanov.mandatory.model.dbms.controller.rest.validation.impl;

import com.barabanov.mandatory.model.dbms.controller.rest.dto.CreateTableDto;
import com.barabanov.mandatory.model.dbms.controller.rest.validation.ColumnNotLowerSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.DatabaseSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.DbSecurityRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Optional;


@RequiredArgsConstructor
public class ColumnSecurityValidator implements ConstraintValidator<ColumnNotLowerSecurity, CreateTableDto>
{
    private final DbSecurityRepository dbSecurityRepository;


    @Override
    public boolean isValid(CreateTableDto value, ConstraintValidatorContext context)
    {
        if (value.getSecurityLevel() == null)
            return true;

        Optional<DatabaseSecurity> dbSecurityOptional = dbSecurityRepository.findById(value.getDbSecId());

        return dbSecurityOptional
                .filter(dbSecurity -> value.getSecurityLevel().getImportantLvl() >= dbSecurity.getSecurityLevel().getImportantLvl())
                .isPresent();
    }
}
