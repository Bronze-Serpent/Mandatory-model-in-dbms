package com.barabanov.mandatory.model.dbms.sql;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


@Service
public class SqlParserImpl implements SqlParser
{

    // Алгоритм разбиения далёк от идеала. Он, мягко говоря, скорее всего не оптимален. Но он работает, а оптимизация потом, при необходимости.
    public ParsedSqlDto parseSelectQuery(String selectSql)
    {
        List<String> parsedSelectSentence = new ArrayList<>();
        String selectRegex = "(?i)select\\b";
        List<String> cutAccordingToSelect = Stream.of(selectSql.split(selectRegex)).toList();
        for (String cutSelect : cutAccordingToSelect)
        {
            String selectSentence = cutSelect.split("(?i)(from|where|order by|having|group by|;)\\b")[0];
            parsedSelectSentence.addAll(
                    Stream.of(selectSentence.split("\\s+|[,()+*/]"))
                            .map(String::trim)
                            .toList()
            );
        }

        List<String> parsedFromSentence = new ArrayList<>();
        String fromRegex = "(?i)from\\b";
        List<String> cutAccordingToFrom = Stream.of(selectSql.split(fromRegex)).toList();
        for (String cutFrom : cutAccordingToFrom)
        {
            String fromSentence = cutFrom.split("(?i)(select|where|order by|having|group by|;)\\b")[0];
            parsedFromSentence.addAll(
                    Stream.of(fromSentence.split("\\s+|[,()+*/]"))
                            .map(String::trim)
                            .toList()
            );
        }

        return new ParsedSqlDto(parsedSelectSentence, parsedFromSentence);
    }
}
