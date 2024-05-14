package com.barabanov.mandatory.model.dbms.controller.rest.dto;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.Authority;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserDto
{
    String login;
    String password;
    Authority authority;
    SecurityLevel securityLevel;
}
