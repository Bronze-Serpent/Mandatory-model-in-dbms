package com.barabanov.mandatory.model.dbms.dynamic.db.security.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Setter
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

    @Builder.Default
    @OneToMany(mappedBy = "tableSecurity")
    List<ColumnSecurity> columnSecurities = new ArrayList<>();
}
