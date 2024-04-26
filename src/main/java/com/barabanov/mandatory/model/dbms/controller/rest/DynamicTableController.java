package com.barabanov.mandatory.model.dbms.controller.rest;

import com.barabanov.mandatory.model.dbms.controller.rest.dto.CreateTableDto;
import com.barabanov.mandatory.model.dbms.controller.rest.dto.UpdateTableSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadTableSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.DynamicTableService;
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
@RequestMapping("api/v1/table")
public class DynamicTableController
{
    private final DynamicTableService dynamicTableService;


    @GetMapping("/{dbId}")
    public List<ReadTableSecDto> getAllTablesForUser(@PathVariable Long dbId)
    {
        return dynamicTableService.getListOfTablesInDb(dbId);
    }


    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ReadTableSecDto createTableInDb(@Valid @RequestBody CreateTableDto createTableDto)
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


    @PutMapping("/security/lvl")
    public ReadTableSecDto changeTableSecLvl(@Valid @RequestBody UpdateTableSecDto tableSecurityDto)
    {
        return dynamicTableService.changeTableSecLvl(tableSecurityDto.getTableSecId(), tableSecurityDto.getSecurityLevel());
    }


    @DeleteMapping("/{tableId}")
    public ResponseEntity<?> dropTable(@NotNull @PathVariable Long tableId)
    {
        dynamicTableService.dropTableInDb(tableId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
