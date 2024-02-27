package com.barabanov.mandatory.model.dbms.dynamic.db.security.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;


@EqualsAndHashCode(of = "name", callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class DbmsAdmin extends AbstractEntity<Long>
{
    private String name;

    @OneToMany(mappedBy = "owner")
    private List<DatabaseSecurity> databases;

    @ManyToMany(mappedBy = "admins")
    private List<DatabaseSecurity> administeredDatabases;
}
