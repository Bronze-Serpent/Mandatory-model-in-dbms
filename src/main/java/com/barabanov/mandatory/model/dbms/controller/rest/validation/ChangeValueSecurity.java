package com.barabanov.mandatory.model.dbms.controller.rest.validation;

import com.barabanov.mandatory.model.dbms.controller.rest.validation.impl.ChangeValueSecurityValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = ChangeValueSecurityValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChangeValueSecurity
{
    String message() default "Нельзя установить уровень секретности значения меньше уровня секретности содержащего его кортежа";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
