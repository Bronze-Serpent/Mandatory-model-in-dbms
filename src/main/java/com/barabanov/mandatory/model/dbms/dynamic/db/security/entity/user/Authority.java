package com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user;

import org.springframework.security.core.GrantedAuthority;


public enum Authority implements GrantedAuthority
{
    SUPER_USER,
    ADMIN,
    USER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
