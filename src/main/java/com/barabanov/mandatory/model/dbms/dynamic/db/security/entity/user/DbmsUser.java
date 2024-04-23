package com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Entity
public class DbmsUser extends AbstractUser
{
    @Enumerated(EnumType.STRING)
    private SecurityLevel securityLevel;
}