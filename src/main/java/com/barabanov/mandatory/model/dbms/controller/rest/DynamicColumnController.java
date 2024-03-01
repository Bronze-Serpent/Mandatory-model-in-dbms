package com.barabanov.mandatory.model.dbms.controller.rest;

import com.barabanov.mandatory.model.dbms.controller.rest.dto.UpdateColumnSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadColumnSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.DynamicColumnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("api/v1/column")
@RestController
public class DynamicColumnController
{
    private final DynamicColumnService dynamicColumnService;


    @PutMapping("/security/lvl")
    public ReadColumnSecDto changeColumnSecLvl(@Valid @RequestBody UpdateColumnSecDto columnSecurityDto)
    {
        return dynamicColumnService.changeColumnSecLvl(columnSecurityDto.getColumnSecId(), columnSecurityDto.getSecurityLvl());
    }
}
