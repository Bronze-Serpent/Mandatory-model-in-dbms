package com.barabanov.mandatory.model.dbms.service;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SqlParserImplTest
{
    @Test
    public void shouldParseSql()
    {
        String regex2 = "(?i)select\\b";

        String sql = """
                    WITH confirms_stat
                             AS
                             (SELECT user_id,
                                     CASE WHEN action IS NULL
                                              THEN 'all_action'
                                          ELSE action
                                         END AS action_info,
                                     COUNT(*) count
                              FROM Confirmations
                              GROUP BY user_id, action WITH ROLLUP),
                         confirms_user_stat AS
                             (SELECT user_id,
                                     (SELECT count confirmed_count
                                      FROM confirms_stat cs
                                      WHERE cs.user_id = s.user_id AND action_info = 'confirmed')
                                         /
                                     (SELECT count confirmed_count
                                      FROM confirms_stat cs
                                      WHERE cs.user_id = s.user_id AND action_info = 'all_action') confirmation_rate
                              FROM Signups s)
                    SELECT user_id,
                           CASE WHEN confirmation_rate IS NULL
                                    THEN 0
                                ELSE ROUND(confirmation_rate, 2)
                               END AS confirmation_rate
                    FROM confirms_user_stat;
                """;

        String sql2 = """
                SELECT a, b, c FROM d1, d2;
                """;

        List<String> list = Stream.of(sql2.split(regex2)).toList();
        List<String> result = new ArrayList<>();
        for (String cutSelect : list)
        {
            String selectLint = cutSelect.split("(?i)(from|where|order by|having|group by|;)\\b")[0];
            result.addAll(
                    Stream.of(selectLint.split("\\s+|[,()+*/]"))
                            .toList()
            );
        }
//       ещё trim делать.
        System.out.println();
    }
}
