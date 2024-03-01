package com.barabanov.mandatory.model.dbms.controller.rest.validation.impl;

import com.barabanov.mandatory.model.dbms.controller.rest.dto.CreateTableDto;
import com.barabanov.mandatory.model.dbms.controller.rest.validation.AvailableTableName;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.TableSecurityRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class TableNameValidator implements ConstraintValidator<AvailableTableName, CreateTableDto>
{
    private final TableSecurityRepository tableSecurityRepository;


    @Override
    public boolean isValid(CreateTableDto value, ConstraintValidatorContext context)
    {
        return tableSecurityRepository.findByNameInDb(value.getDbSecId(), value.getTableName()).isEmpty();
    }
}
