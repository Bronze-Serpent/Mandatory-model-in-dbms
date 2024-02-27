package com.barabanov.mandatory.model.dbms.controller;

import com.barabanov.mandatory.model.dbms.controller.dto.ReadValueSecDto;
import com.barabanov.mandatory.model.dbms.controller.dto.UpdateValueSecurityDto;
import com.barabanov.mandatory.model.dbms.service.iterface.DynamicValService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/value")
public class DynamicValueController
{
    private final DynamicValService dynamicValService;


    @PutMapping("/change/lvl")
    public ReadValueSecDto changeValueSecLvl(UpdateValueSecurityDto valueSecDto)
    {
        return dynamicValService.changeValueSecLvl(
                valueSecDto.getTupleId(),
                valueSecDto.getColumnSecId(),
                valueSecDto.getSecurityLevel()
        );
    }
}
