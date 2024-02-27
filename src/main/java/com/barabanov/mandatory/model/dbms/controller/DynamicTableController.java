package com.barabanov.mandatory.model.dbms.controller;

import com.barabanov.mandatory.model.dbms.controller.dto.CreateTableDto;
import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.service.iterface.SecureDynamicTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/table")
public class DynamicTableController
{
    private final SecureDynamicTableService dynamicTableService;


    @PostMapping("/create")
    public ResponseEntity<?> createTableInDb(@RequestBody CreateTableDto createTableDto)
    {
        if (createTableDto.getSecurityLevel() == null)
            dynamicTableService.createTableInDb(
                    createTableDto.getDbSecId(),
                    createTableDto.getTableName(),
                    createTableDto.getColumnsDesc()
            );
        else
            dynamicTableService.createTableInDb(
                    createTableDto.getDbSecId(),
                    createTableDto.getTableName(),
                    createTableDto.getColumnsDesc(),
                    createTableDto.getSecurityLevel()
            );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }


    @PutMapping("/{tableId}")
    public ResponseEntity<?> changeTableSecLvl(@PathVariable Long tableId,
                                               @RequestParam SecurityLevel newSecLvl)
    {
        dynamicTableService.changeTableSecLvl(tableId, newSecLvl);

        return ResponseEntity
                .ok()
                .build();
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
