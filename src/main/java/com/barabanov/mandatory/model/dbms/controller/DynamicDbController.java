package com.barabanov.mandatory.model.dbms.controller;

import com.barabanov.mandatory.model.dbms.controller.dto.CreateDbDto;
import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.service.iterface.SecureDynamicDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


// TODO: 26.02.2024 validation, Security, ExceptionHandler
// TODO: 26.02.2024 более внятные Http статусы
// TODO: 26.02.2024 информативные ответы. На создание - созданная сущность, при обновлении - обновлённая
@RequiredArgsConstructor
@RestController
@RequestMapping(name = "api/v1/db")
public class DynamicDbController
{
    private final SecureDynamicDbService secureDynamicDbService;


    @PostMapping("/create")
    public ResponseEntity<?> createDb(CreateDbDto createDbDto)
    {
        secureDynamicDbService.createDb(createDbDto.getName(), createDbDto.getSecurityLevel());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }


    @PutMapping("/{dbId}")
    public ResponseEntity<?> changeDbSecurity(@PathVariable Long dbId,
                                              @RequestParam SecurityLevel newSecurityLvl)
    {
        secureDynamicDbService.changeDbSecLvl(dbId, newSecurityLvl);

        return ResponseEntity
                .ok()
                .build();
    }


    @DeleteMapping("/{dbId}")
    public ResponseEntity<?> deleteDb(@PathVariable Long dbId)
    {
        secureDynamicDbService.deleteDb(dbId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
