package com.barabanov.mandatory.model.dbms.controller.rest.handler;

import com.barabanov.mandatory.model.dbms.controller.rest.dto.ExceptionDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.exception.DynamicDatabaseNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler
{
    @ExceptionHandler(DynamicDatabaseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto handleDynamicDbException(DynamicDatabaseNotFoundException dynamicDbException)
    {
        return new ExceptionDto(dynamicDbException.toString());
    }

    // TODO: 27.02.2024 Не очень здорово, что именно BadSqlGrammarException.
    //  Думаю, лучше перехватывать эти exception и возвращать свои в местах где могут выпадать BadSqlGrammarException в динамических БД т.к.
    //  в приложении тоже есть БД и там тоже могут возникать эти исключения.
    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleDynamicSqlException(BadSqlGrammarException sqlGrammarException)
    {
        return new ExceptionDto(sqlGrammarException.toString());
    }
}
