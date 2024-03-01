package com.barabanov.mandatory.model.dbms.controller.rest.validation;

import com.barabanov.mandatory.model.dbms.controller.rest.validation.impl.ColumnSecurityValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = ColumnSecurityValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnNotLowerSecurity
{
    String message() default "Нельзя создать колонку с меньшим уровнем секретности по сравнению с содержащей её таблицей";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
