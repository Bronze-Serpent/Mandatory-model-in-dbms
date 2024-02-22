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
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ValueSecurity extends AbstractEntity<Long>
{
    private Long tupleId;

    @ManyToOne
    @JoinColumn(name = "column_id")
    private ColumnSecurity columnSecurity;

    SecurityLevel securityLevel;
}
