package com.barabanov.mandatory.model.dbms.dynamic.db.security.repository;


import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.AbstractUser;
import org.springframework.data.repository.Repository;

import java.util.Optional;


public interface AbstractUserRepository extends Repository<AbstractUser, Long>
{
    Optional<AbstractUser> findByLogin(String login);
}
