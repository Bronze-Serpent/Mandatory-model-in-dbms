package com.barabanov.mandatory.model.dbms.controller.rest.dto;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.Authority;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@Data
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserReadDto
{
    Long id;
    String login;
    Authority authority;
    SecurityLevel securityLevel;
}
