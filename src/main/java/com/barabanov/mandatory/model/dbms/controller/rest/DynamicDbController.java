package com.barabanov.mandatory.model.dbms.controller.rest;

import com.barabanov.mandatory.model.dbms.controller.rest.dto.CreateDbDto;
import com.barabanov.mandatory.model.dbms.controller.rest.dto.UpdateDbSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadDbSecAdminDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadDbSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.DynamicDbService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(name = "api/v1/db")
public class DynamicDbController
{
    private final DynamicDbService dynamicDbService;


    @GetMapping("/all")
    public Object getAllDbForUser()
    {
        return dynamicDbService.getDatabasesList();
    }


    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ReadDbSecAdminDto createDb(@Valid @RequestBody CreateDbDto createDbDto)
    {
        return dynamicDbService.createDb(createDbDto.getName(), createDbDto.getSecurityLevel());
    }


    @PutMapping("/security/lvl")
    public ReadDbSecAdminDto changeDbSecurity(@Valid @RequestBody UpdateDbSecDto dbSecurityDto)
    {
        return dynamicDbService.changeDbSecLvl(dbSecurityDto.getDbSecId(), dbSecurityDto.getSecurityLevel());
    }


    @DeleteMapping("/{dbId}")
    public ResponseEntity<?> deleteDb(@NotNull @PathVariable Long dbId)
    {
        dynamicDbService.deleteDb(dbId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
