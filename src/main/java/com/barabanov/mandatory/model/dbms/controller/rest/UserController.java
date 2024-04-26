package com.barabanov.mandatory.model.dbms.controller.rest;

import com.barabanov.mandatory.model.dbms.controller.rest.dto.CreateUserDto;
import com.barabanov.mandatory.model.dbms.controller.rest.dto.UserReadDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/user")
public class UserController
{
    private final UserService userService;


    @PostMapping("/create")
    public UserReadDto creteUser(CreateUserDto createUserDto)
    {
        return userService.createUser(createUserDto);
    }
}
