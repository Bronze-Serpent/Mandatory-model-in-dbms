package com.barabanov.mandatory.model.dbms.controller.rest.validation;

import com.barabanov.mandatory.model.dbms.controller.rest.validation.impl.ChangeTableSecurityValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = ChangeTableSecurityValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChangeTableSecurity
{
    String message() default "Нельзя установить уровень секретности таблицы меньше уровня секретности содержащей её базы данных";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
