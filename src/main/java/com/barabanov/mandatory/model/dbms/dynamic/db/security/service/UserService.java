package com.barabanov.mandatory.model.dbms.dynamic.db.security.service;

import com.barabanov.mandatory.model.dbms.controller.rest.dto.CreateUserDto;
import com.barabanov.mandatory.model.dbms.controller.rest.dto.UserReadDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.Authority;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.DbmsAdmin;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.user.DbmsUser;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.mapper.UserMapper;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.AbstractUserRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.AdminRepository;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.repository.UserRepository;
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
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


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


    public UserReadDto createUser(CreateUserDto createUserDto)
    {
        if (createUserDto.getAuthority() == USER)
        {
            DbmsUser user = userMapper.toUser(createUserDto);
            return userMapper.toUserDto(userRepository.save(user));
        }
        else
            if (createUserDto.getAuthority() == ADMIN)
            {
                DbmsAdmin admin = userMapper.toAdmin(createUserDto);
                return userMapper.toUserDto(adminRepository.save(admin));
            }
            else
                throw new RuntimeException("Нельзя создать ещё одного пользователя с пролью SUPER_USER");
    }
}
