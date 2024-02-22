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
    private static final String SECURITY_SQL_WORD_PATTERN = "\\s*-{1,2}\\s*[a-zA-Z_]+";
    private static final String VALUE_SECURITY_IDENTIFICATION = "-";
    private static final String TUPLE_SECURITY_IDENTIFICATION = "--";


    public ParsedSecretSqlDto parse(String securitySql)
    {
        String[] words = securitySql.split(SECURITY_SQL_SPLIT_REGEX);

        return new ParsedSecretSqlDto(
                words[2],
                getCleanedSql(securitySql),
                getValueSecurityList(getColumnNames(words), words),
                getTupleSecurity(words)
        );
    }


    private SecurityLevel getTupleSecurity(String[] securitySqlWords)
    {
        if (securitySqlWords[securitySqlWords.length - 2].equals(TUPLE_SECURITY_IDENTIFICATION))
        {
            String securityLvlString = securitySqlWords[securitySqlWords.length - 1]
                    .replaceAll(";", "");
            return SecurityLevel.fromString(securityLvlString);
        }

        return null;
    }


    private String getCleanedSql(String securitySql)
    {
        return securitySql.replaceAll(SECURITY_SQL_WORD_PATTERN, "");
    }


    private List<ValueSecurityInfo> getValueSecurityList(List<String> columnNames, String[] securitySqlWords)
    {
        int colIdx = 0;
        List<ValueSecurityInfo> valueSecurityInfos = new ArrayList<>();
        int valIdx = 4 + columnNames.size();

        while (valIdx < securitySqlWords.length - 1)
        {
            if (!securitySqlWords[valIdx + 1].equals(VALUE_SECURITY_IDENTIFICATION))
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
