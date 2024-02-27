package com.barabanov.mandatory.model.dbms.service;

import com.barabanov.mandatory.model.dbms.secure.sql.dto.ParsedSecretSqlDto;
import com.barabanov.mandatory.model.dbms.secure.sql.dto.ValueSecurityInfo;
import com.barabanov.mandatory.model.dbms.secure.sql.service.SecuritySqlParserImpl;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;


@Profile("test")
@SpringBootTest
@RequiredArgsConstructor
public class SecuritySqlParserImplTest
{
    private final SecuritySqlParserImpl securitySqlParser;


    @SuppressWarnings("ConstantConditions")
    @Test
    public void shouldCleanSecuritySql() throws InvocationTargetException, IllegalAccessException
    {
        String securitySql = """
                INSERT INTO table_1 (col_1, col2, col_3)
                VALUES ('text_1' - TOP_SECRET, 123 - OF_PARTICULAR_IMPORTANCE, '20.02.2024') -- SECRET;
                """;
        String cleanedSql = """
                INSERT INTO table_1 (col_1, col2, col_3)
                VALUES ('text_1', 123, '20.02.2024');
                """;

        Method getCleanedSqlMethod = ReflectionUtils.findMethod(SecuritySqlParserImpl.class, "getCleanedSql", String.class);
        ReflectionUtils.makeAccessible(getCleanedSqlMethod);

        String cleanedSqlResult = (String) getCleanedSqlMethod.invoke(securitySqlParser, securitySql);

        assertThat(cleanedSqlResult).isEqualTo(cleanedSql);
    }


    @Test
    public void shouldCreateValueSecurityList()
    {
        String securitySql = """
                INSERT INTO table_1 (col_1, col2, col_3)
                VALUES ('text_1' - TOP_SECRET, 123 - OF_PARTICULAR_IMPORTANCE, '20.02.2024') -- SECRET;
                """;
        ParsedSecretSqlDto parsedDto = securitySqlParser.parse(securitySql);

        assertThat(parsedDto.getValueSecurityInfoList()).containsExactlyInAnyOrder(
                new ValueSecurityInfo("col_1", TOP_SECRET),
                new ValueSecurityInfo("col2", OF_PARTICULAR_IMPORTANCE)
        );
    }


    @Test
    public void shouldHandleLastValueSecurity()
    {
        String securitySql = """
                INSERT INTO table_1 (col_1, col2, col_3)
                VALUES ('text_1', 123 - OF_PARTICULAR_IMPORTANCE, '20.02.2024'  - TOP_SECRET) -- SECRET;
                """;
        ParsedSecretSqlDto parsedDto = securitySqlParser.parse(securitySql);

        assertThat(parsedDto.getValueSecurityInfoList()).containsExactlyInAnyOrder(
                new ValueSecurityInfo("col_3", TOP_SECRET),
                new ValueSecurityInfo("col2", OF_PARTICULAR_IMPORTANCE)
        );
    }


    @Test
    public void shouldExtractTupleSecurity()
    {
        String securitySql = """
                INSERT INTO table_1 (col_1, col2, col_3)
                VALUES ('text_1', 123 - OF_PARTICULAR_IMPORTANCE, '20.02.2024'  - TOP_SECRET) -- SECRET;
                """;
        ParsedSecretSqlDto parsedDto = securitySqlParser.parse(securitySql);

        assertThat(parsedDto.getRowSecurityLvl()).isEqualTo(SECRET);
    }


    @Test
    public void shouldExtractTupleSecurityWithoutException()
    {
        String securitySql = """
                INSERT INTO table_1 (col_1, col2, col_3)
                VALUES ('text_1', 123 - OF_PARTICULAR_IMPORTANCE, '20.02.2024'  - TOP_SECRET);
                """;

        assertThatNoException().isThrownBy(() -> securitySqlParser.parse(securitySql));

        ParsedSecretSqlDto parsedDto = securitySqlParser.parse(securitySql);

        assertThat(parsedDto.getRowSecurityLvl()).isEqualTo(null);
    }
}
