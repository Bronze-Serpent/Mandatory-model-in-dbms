package com.barabanov.mandatory.model.dbms.controller;

import com.barabanov.mandatory.model.dbms.controller.dto.UpdateValueSecurityDto;
import com.barabanov.mandatory.model.dbms.service.iterface.SecureDynamicValService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/value")
public class DynamicValueController
{
    private final SecureDynamicValService dynamicValService;


    @PutMapping("/change/lvl")
    public ResponseEntity<?> changeValueSecLvl(UpdateValueSecurityDto valueSecDto)
    {
        dynamicValService.changeValueSecLvl(
                valueSecDto.getTupleId(),
                valueSecDto.getColumnSecId(),
                valueSecDto.getSecurityLevel()
        );

        return ResponseEntity
                .ok()
                .build();
    }
}
