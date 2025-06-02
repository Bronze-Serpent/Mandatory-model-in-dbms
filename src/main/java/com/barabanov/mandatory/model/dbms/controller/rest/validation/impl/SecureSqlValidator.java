package com.barabanov.mandatory.model.dbms.controller.rest.validation.impl;

import com.barabanov.mandatory.model.dbms.controller.rest.validation.ValidSecureSql;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;


public class SecureSqlValidator implements ConstraintValidator<ValidSecureSql, String> {
    private static final Set<String> sqlCmdWords = Set.of("ALTER", "CREATE", "DELETE", "UPDATE", "SELECT", "DROP", "EXEC", "EXIST", "INSERT");
    private static final String SECURITY_SQL_SPLIT_REGEX = "\\s*[(,)\\s]+\\s*";


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String[] secureSqlWords = value.trim().split(SECURITY_SQL_SPLIT_REGEX);
        if (!secureSqlWords[0].equals("INSERT") || !secureSqlWords[1].equals("INTO"))
            return false;

        for (int i = 2; i < secureSqlWords.length; i++)
            if (sqlCmdWords.contains(secureSqlWords[i]))
                return false;

        return true;
    }
}
