package com.barabanov.mandatory.model.dbms.dynamic.db.security.repository;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.DbmsUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.Optional;


public interface UserRepository extends CrudRepository<DbmsUser, Long>
{
    @Query("select d.securityLevel " +
            "from DbmsUser d " +
            "where d.login = :login")
    Optional<SecurityLevel> findSecurityLevelByLogin(String login);
}
