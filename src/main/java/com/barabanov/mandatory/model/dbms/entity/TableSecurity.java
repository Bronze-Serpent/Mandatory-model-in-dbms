package com.barabanov.mandatory.model.dbms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TableSecurity extends AbstractEntity<Long>
{
    private String name;

    @Enumerated(EnumType.STRING)
    SecurityLevel securityLevel;

    @ManyToOne
    @JoinColumn(name = "database_id")
    DatabaseSecurity databaseSecurity;
}
