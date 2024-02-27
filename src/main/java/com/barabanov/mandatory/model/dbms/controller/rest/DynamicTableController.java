package com.barabanov.mandatory.model.dbms.controller.rest;

import com.barabanov.mandatory.model.dbms.controller.rest.dto.CreateTableDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadTableSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.DynamicTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/table")
public class DynamicTableController
{
    private final DynamicTableService dynamicTableService;


    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ReadTableSecDto createTableInDb(@RequestBody CreateTableDto createTableDto)
    {
        if (createTableDto.getSecurityLevel() == null)
            return dynamicTableService.createTableInDb(
                    createTableDto.getDbSecId(),
                    createTableDto.getTableName(),
                    createTableDto.getColumnsDesc()
            );
        return dynamicTableService.createTableInDb(
                createTableDto.getDbSecId(),
                createTableDto.getTableName(),
                createTableDto.getColumnsDesc(),
                createTableDto.getSecurityLevel()
        );
    }


    @PutMapping("/{tableId}")
    public ReadTableSecDto changeTableSecLvl(@PathVariable Long tableId,
                                             @RequestParam SecurityLevel newSecLvl)
    {
        return dynamicTableService.changeTableSecLvl(tableId, newSecLvl);
    }


    @DeleteMapping("/{tableId}")
    public ResponseEntity<?> dropTable(@PathVariable Long tableId)
    {
        dynamicTableService.dropTableInDb(tableId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
