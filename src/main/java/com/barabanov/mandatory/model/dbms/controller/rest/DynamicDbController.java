package com.barabanov.mandatory.model.dbms.controller.rest;

import com.barabanov.mandatory.model.dbms.controller.rest.dto.CreateDbDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadDbSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.DynamicDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


// TODO: 26.02.2024 Security
//  validation

@RequiredArgsConstructor
@RestController
@RequestMapping(name = "api/v1/db")
public class DynamicDbController
{
    private final DynamicDbService dynamicDbService;


    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ReadDbSecDto createDb(CreateDbDto createDbDto)
    {
        return dynamicDbService.createDb(createDbDto.getName(), createDbDto.getSecurityLevel());
    }


    @PutMapping("/{dbId}")
    public ReadDbSecDto changeDbSecurity(@PathVariable Long dbId,
                                         @RequestParam SecurityLevel newSecurityLvl)
    {
        return dynamicDbService.changeDbSecLvl(dbId, newSecurityLvl);
    }


    @DeleteMapping("/{dbId}")
    public ResponseEntity<?> deleteDb(@PathVariable Long dbId)
    {
        dynamicDbService.deleteDb(dbId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
