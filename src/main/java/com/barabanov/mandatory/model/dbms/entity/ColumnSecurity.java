package com.barabanov.mandatory.model.dbms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ColumnSecurity extends AbstractEntity<Long>
{
    private String name;

    @Enumerated(EnumType.STRING)
    private SecurityLevel securityLevel;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private TableSecurity tableSecurity;
}
