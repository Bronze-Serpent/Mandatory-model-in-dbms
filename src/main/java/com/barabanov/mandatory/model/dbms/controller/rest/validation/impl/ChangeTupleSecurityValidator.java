package com.barabanov.mandatory.model.dbms.controller.rest.validation.impl;

import com.barabanov.mandatory.model.dbms.controller.rest.dto.UpdateTupleSecDto;
import com.barabanov.mandatory.model.dbms.controller.rest.validation.ChangeTupleSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.TableSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.TableSecurityRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Optional;


@RequiredArgsConstructor
public class ChangeTupleSecurityValidator implements ConstraintValidator<ChangeTupleSecurity, UpdateTupleSecDto>
{
    private final TableSecurityRepository tableSecurityRepository;


    @Override
    public boolean isValid(UpdateTupleSecDto value, ConstraintValidatorContext context)
    {
        if (value.getNewSecurityLvl() == null)
            return false;

        Optional<TableSecurity> tableSecOptional = tableSecurityRepository.findById(value.getTableSecId());

        return tableSecOptional
                .filter(tableSec -> value.getNewSecurityLvl().getImportantLvl() >= tableSec.getSecurityLevel().getImportantLvl())
                .isPresent();
    }
}
