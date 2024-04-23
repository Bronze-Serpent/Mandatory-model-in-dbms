package com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.DatabaseSecurity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;

import java.util.List;


@Getter
@Entity
public class DbmsAdmin extends AbstractUser
{
    @OneToMany(mappedBy = "owner")
    private List<DatabaseSecurity> databases;

    @ManyToMany(mappedBy = "admins")
    private List<DatabaseSecurity> administeredDatabases;
}
