package com.barabanov.mandatory.model.dbms.controller.rest.handler;

import com.barabanov.mandatory.model.dbms.controller.rest.dto.ExceptionDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.exception.DynamicDatabaseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
public class ControllerExceptionHandler
{
    @ExceptionHandler(DynamicDatabaseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto handleDynamicDbException(DynamicDatabaseNotFoundException dynamicDbException)
    {
        return new ExceptionDto(dynamicDbException.toString());
    }


    // TODO: 27.02.2024 Не очень здорово, что именно BadSqlGrammarException.
    //  Думаю, лучше перехватывать BadSqlGrammarException и возвращать свои в местах где могут выпадать BadSqlGrammarException в динамических БД т.к.
    //  в приложении тоже есть БД и там тоже могут возникать BadSqlGrammarException исключения.
    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleDynamicSqlException(BadSqlGrammarException sqlGrammarException)
    {
        return new ExceptionDto("Exception during execution sql: " + sqlGrammarException.getSql());
    }


    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(BindingResult bindingResult)
    {
        Map<String, String> exceptionDesMap = new HashMap<>();

        for (ObjectError objectError : bindingResult.getAllErrors())
        {
            exceptionDesMap.put(objectError.getObjectName(), objectError.getDefaultMessage());
        }

        return exceptionDesMap;
    }

}
