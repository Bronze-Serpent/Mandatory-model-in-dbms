package com.barabanov.mandatory.model.dbms.controller.rest.validation.impl;

import com.barabanov.mandatory.model.dbms.controller.rest.dto.UpdateColumnSecDto;
import com.barabanov.mandatory.model.dbms.controller.rest.validation.ChangeColumnSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.ColumnSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.ColumnSecurityRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Optional;


@RequiredArgsConstructor
public class ChangeColumnSecurityValidator implements ConstraintValidator<ChangeColumnSecurity, UpdateColumnSecDto>
{
    private final ColumnSecurityRepository columnSecurityRepository;


    @Override
    public boolean isValid(UpdateColumnSecDto value, ConstraintValidatorContext context)
    {
        if (value.getSecurityLvl() == null)
            return false;

        Optional<ColumnSecurity> columnSecurity = columnSecurityRepository.findById(value.getColumnSecId());
        return columnSecurity.filter(security -> security
                .getTableSecurity()
                .getSecurityLevel()
                .getImportantLvl() <= value.getSecurityLvl().getImportantLvl())
                .isPresent();
    }
}
