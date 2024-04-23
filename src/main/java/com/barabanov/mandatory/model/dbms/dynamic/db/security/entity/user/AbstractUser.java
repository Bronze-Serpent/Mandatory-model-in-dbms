package com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "login", callSuper = false)
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
public abstract class AbstractUser extends AbstractEntity<Long>
{
    private String login;

    private String password;
}
