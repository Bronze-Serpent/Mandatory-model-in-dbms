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
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TupleSecurity extends AbstractEntity<Long>
{
    private Long tupleId;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private TableSecurity tableSecurity;

    @Enumerated(EnumType.STRING)
    private SecurityLevel securityLevel;
}
