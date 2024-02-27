package com.barabanov.mandatory.model.dbms.controller;

import com.barabanov.mandatory.model.dbms.service.dto.ReadColumnSecDto;
import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.service.iterface.DynamicColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("api/v1/column")
@RestController
public class DynamicColumnController
{
    private final DynamicColumnService dynamicColumnService;


    @PutMapping("{columnId}")
    public ReadColumnSecDto changeColumnSecLvl(@PathVariable Long columnId,
                                               @RequestParam SecurityLevel newColumnSecLvl)
    {

        return dynamicColumnService.changeColumnSecLvl(columnId, newColumnSecLvl);
    }
}
