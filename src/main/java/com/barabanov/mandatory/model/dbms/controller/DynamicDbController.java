package com.barabanov.mandatory.model.dbms.controller;

import com.barabanov.mandatory.model.dbms.controller.dto.CreateDbDto;
import com.barabanov.mandatory.model.dbms.controller.dto.ReadDbSecDto;
import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.service.iterface.DynamicDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


// TODO: 26.02.2024 validation, Security, ExceptionHandler
// TODO: 26.02.2024 более внятные Http статусы                                                                     -done
// TODO: 26.02.2024 информативные ответы. На создание - созданная сущность, при обновлении - обновлённая
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
