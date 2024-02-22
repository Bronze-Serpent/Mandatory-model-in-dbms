package com.barabanov.mandatory.model.dbms.service;

import com.barabanov.mandatory.model.dbms.dto.ParsedSecretSqlDto;
import com.barabanov.mandatory.model.dbms.dto.ValueSecurityInfo;
import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class SecuritySqlParser
{

    private static final String SECURITY_SQL_SPLIT_REGEX = "\\s*[(,)\\s]+\\s*";
    private static final String SECURITY_SQL_WORD_PATTERN = "-\\s*[a-zA-Z_]+";


    public ParsedSecretSqlDto parse(String securitySql)
    {
        String[] words = securitySql.split(SECURITY_SQL_SPLIT_REGEX);

        return new ParsedSecretSqlDto(
                words[2],
                getCleanedSql(securitySql),
                getValueSecurityList(getColumnNames(words), words),
                null
        );
    }


    private String getCleanedSql(String securitySql)
    {
        return securitySql.replaceAll(SECURITY_SQL_WORD_PATTERN, "");
    }


    private List<ValueSecurityInfo> getValueSecurityList(List<String> columnNames, String[] securitySqlWords)
    {
        int colIdx = 0;
        List<ValueSecurityInfo> valueSecurityInfos = new ArrayList<>();
        int valIdx = 3 + columnNames.size();

        while (valIdx < securitySqlWords.length - 1)
        {
            if (!securitySqlWords[valIdx + 1]. equals("-"))
            {
                valIdx++;
                colIdx++;
                continue;
            }
            valIdx += 2;
            valueSecurityInfos.add(new ValueSecurityInfo(columnNames.get(colIdx), SecurityLevel.fromString(securitySqlWords[valIdx])));
            valIdx++;
            colIdx++;
        }

        return valueSecurityInfos;
    }


    private List<String> getColumnNames(String[] securitySqlWords)
    {
        String columnNamesEndWord = "VALUES";
        int wordIdx = 3;
        List<String> columnNames = new ArrayList<>();
        while (!securitySqlWords[wordIdx].equalsIgnoreCase(columnNamesEndWord))
        {
            columnNames.add(securitySqlWords[wordIdx]);
            wordIdx++;
        }

        return columnNames;
    }
}
