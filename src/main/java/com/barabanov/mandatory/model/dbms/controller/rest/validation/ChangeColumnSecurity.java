package com.barabanov.mandatory.model.dbms.controller.rest.validation;

import com.barabanov.mandatory.model.dbms.controller.rest.validation.impl.ChangeColumnSecurityValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = ChangeColumnSecurityValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChangeColumnSecurity
{
    String message() default "Нельзя установить уровень секретности колонки меньше уровня секретности содержащей её таблицы";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
