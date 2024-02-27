package com.barabanov.mandatory.model.dbms.dynamic.db.security.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
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


    @Builder.Default
    @ManyToMany
    @JoinTable(name = "database_administrator",
            joinColumns = {@JoinColumn(name = "database_id")},
            inverseJoinColumns = {@JoinColumn(name = "admin_id")})
    private List<DbmsAdmin> admins = new ArrayList<>();
}
