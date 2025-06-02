package com.barabanov.mandatory.model.dbms.controller.rest.validation.impl;

import com.barabanov.mandatory.model.dbms.controller.rest.dto.UpdateValueSecDto;
import com.barabanov.mandatory.model.dbms.controller.rest.validation.ChangeValueSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.TupleSecurity;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.TupleSecurityRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Optional;


@RequiredArgsConstructor
public class ChangeValueSecurityValidator implements ConstraintValidator<ChangeValueSecurity, UpdateValueSecDto>
{
    private final TupleSecurityRepository tupleSecurityRepository;


    @Override
    public boolean isValid(UpdateValueSecDto value, ConstraintValidatorContext context)
    {
        if (value.getSecurityLevel() == null)
            return false;
        // TODO: 03.03.2024 если нет записи об уровне секретности кортежа, то работать будет неправильно
        Optional<TupleSecurity> tupleSecOptional = tupleSecurityRepository.findById(value.getTupleId());
        return tupleSecOptional
                .filter(tupleSec -> value.getSecurityLevel().getImportantLvl() >= tupleSec.getSecurityLevel().getImportantLvl())
                .isPresent();
    }
}
