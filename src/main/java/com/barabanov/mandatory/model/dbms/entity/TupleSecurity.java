package com.barabanov.mandatory.model.dbms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TupleSecurity extends AbstractEntity<Long>
{
    private Long tupleId;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private TableSecurity tableSecurity;

    private SecurityLevel securityLevel;
}
