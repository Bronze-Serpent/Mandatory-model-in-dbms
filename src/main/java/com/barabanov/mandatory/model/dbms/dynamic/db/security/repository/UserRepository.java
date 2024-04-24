package com.barabanov.mandatory.model.dbms.dynamic.db.security.repository;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.DbmsUser;
import org.springframework.data.repository.Repository;

import java.util.Optional;


public interface UserRepository extends Repository<DbmsUser, Long>
{
    Optional<DbmsUser> findByLogin(String login);
}
