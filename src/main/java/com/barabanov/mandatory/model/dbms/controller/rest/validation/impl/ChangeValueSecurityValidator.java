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

        Optional<TupleSecurity> tupleSecOptional = tupleSecurityRepository.findById(value.getTupleId());
        return tupleSecOptional
                .filter(tupleSec -> value.getSecurityLevel().getImportantLvl() >= tupleSec.getSecurityLevel().getImportantLvl())
                .isPresent();
    }
}
