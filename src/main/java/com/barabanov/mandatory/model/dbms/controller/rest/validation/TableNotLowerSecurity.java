package com.barabanov.mandatory.model.dbms.controller.rest.validation;

import com.barabanov.mandatory.model.dbms.controller.rest.validation.impl.TableSecurityValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = TableSecurityValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableNotLowerSecurity
{
    String message() default "Нельзя создать таблицу с меньшим уровнем секретности по сравнению с содержащей её базой данных";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
