package com.barabanov.mandatory.model.dbms.controller.rest;

import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadValueSecDto;
import com.barabanov.mandatory.model.dbms.controller.rest.dto.UpdateValueSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.DynamicValService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/value")
public class DynamicValueController
{
    private final DynamicValService dynamicValService;


    @PutMapping("/change/lvl")
    public ReadValueSecDto changeValueSecLvl(@Valid @RequestBody UpdateValueSecDto valueSecDto)
    {
        return dynamicValService.changeValueSecLvl(
                valueSecDto.getTupleId(),
                valueSecDto.getColumnSecId(),
                valueSecDto.getSecurityLevel()
        );
    }
}
