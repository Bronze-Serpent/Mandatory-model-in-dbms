package com.barabanov.mandatory.model.dbms.sql;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ParsedSqlDto
{
    List<String> parsedSelectSentence;
    List<String> parsedFromSentence;
}
