package com.barabanov.mandatory.model.dbms.entity;

import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "database_security")
public class DatabaseSecurity extends AbstractEntity<Long>
{
    private String name;

    @Enumerated(EnumType.STRING)
    private SecurityLevel securityLevel;

    @ManyToOne
    @JoinTable(name = "database_ownership",
            joinColumns = {@JoinColumn(name = "database_id")},
            inverseJoinColumns = {@JoinColumn(name = "admin_id")})
    private DbmsAdmin owner;


    @ManyToMany
    @JoinTable(name = "database_administrator",
            joinColumns = {@JoinColumn(name = "database_id")},
            inverseJoinColumns = {@JoinColumn(name = "admin_id")})
    private List<DbmsAdmin> admins;
}
