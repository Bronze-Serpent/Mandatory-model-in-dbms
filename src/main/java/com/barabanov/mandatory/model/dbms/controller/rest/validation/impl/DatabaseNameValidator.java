package com.barabanov.mandatory.model.dbms.controller.rest.validation.impl;

import com.barabanov.mandatory.model.dbms.controller.rest.validation.AvailableDatabaseName;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.DbSecurityRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class DatabaseNameValidator implements ConstraintValidator<AvailableDatabaseName, String>
{
    // TODO: 28.02.2024 что насчёт транзакций при таком использовании
    private final DbSecurityRepository dbSecurityRepository;


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context)
    {
        return dbSecurityRepository.findByName(value).isEmpty();
    }
}
