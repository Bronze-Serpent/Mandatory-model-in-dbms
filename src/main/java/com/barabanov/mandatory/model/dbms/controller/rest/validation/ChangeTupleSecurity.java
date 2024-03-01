package com.barabanov.mandatory.model.dbms.controller.rest.validation;

import com.barabanov.mandatory.model.dbms.controller.rest.validation.impl.ChangeTupleSecurityValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = ChangeTupleSecurityValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChangeTupleSecurity
{
    String message() default "Нельзя установить уровень секретности кортежа меньше уровня секретности содержащей её таблицы";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
