package com.barabanov.mandatory.model.dbms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@EqualsAndHashCode(of = "name", callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class DbmsUser extends AbstractEntity<Long>
{
    private String name;

    @Enumerated(EnumType.STRING)
    private SecurityLevel securityLevel;
}