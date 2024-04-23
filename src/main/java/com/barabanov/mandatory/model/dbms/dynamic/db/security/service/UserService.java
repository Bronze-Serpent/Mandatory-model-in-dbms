package com.barabanov.mandatory.model.dbms.dynamic.db.security.service;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.Authority;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.DbmsAdmin;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.DbmsUser;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.AbstractUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

import static com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.Authority.*;


@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService
{

    private final AbstractUserRepository abstractUserRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        return abstractUserRepository.findByLogin(username)
                .map(abstractUser ->
                {
                    Authority authority;
                    if (abstractUser instanceof DbmsUser)
                        authority = USER;
                    else
                    {
                        if (abstractUser instanceof DbmsAdmin)
                            authority = ADMIN;
                        else
                            authority = SUPER_USER;
                    }
                    return new User(
                            abstractUser.getLogin(),
                            abstractUser.getPassword(),
                            Collections.singleton(authority));
                })
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь под login: " + username + " не найден."));
    }
}
