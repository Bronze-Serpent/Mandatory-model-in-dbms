package com.barabanov.mandatory.model.dbms.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ValueSecurity extends AbstractEntity<Long>
{
    private Long tupleId;

    @ManyToOne
    @JoinColumn(name = "column_id")
    private ColumnSecurity columnSecurity;

    @Enumerated(EnumType.STRING)
    SecurityLevel securityLevel;
}
