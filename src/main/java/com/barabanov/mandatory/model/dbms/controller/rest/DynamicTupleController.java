package com.barabanov.mandatory.model.dbms.controller.rest;

import com.barabanov.mandatory.model.dbms.controller.rest.dto.CreateTupleDto;
import com.barabanov.mandatory.model.dbms.controller.rest.dto.DeleteTupleDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.dto.ReadTupleSecurityDto;
import com.barabanov.mandatory.model.dbms.controller.rest.dto.UpdateTupleSecDto;
import com.barabanov.mandatory.model.dbms.dynamic.db.security.service.iterface.DynamicTupleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("api/v1/tuple")
@RestController
public class DynamicTupleController
{
    private final DynamicTupleService dynamicTupleService;


//    @GetMapping("/select")
//    public String selectFromDynamicDb()
//    {
//
//    }


    @PostMapping("/insert")
    @ResponseStatus(HttpStatus.CREATED)
    public ReadTupleSecurityDto insertTupleIntoDb(@Valid @RequestBody CreateTupleDto createTupleDto)
    {
        return dynamicTupleService.insertIntoDb(createTupleDto.getDbSecId(), createTupleDto.getSecureSql());
    }


    @PutMapping("/security/lvl")
    public ReadTupleSecurityDto changeTupleSecLvl(@Valid @RequestBody UpdateTupleSecDto updateTupleDto)
    {
        return dynamicTupleService.changeTupleSecLvl(
                updateTupleDto.getTableSecId(),
                updateTupleDto.getTupleId(),
                updateTupleDto.getNewSecurityLvl());
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTuple(@Valid @RequestBody DeleteTupleDto deleteTupleDto)
    {
        dynamicTupleService.deleteTupleInTable(deleteTupleDto.getTableSecId(), deleteTupleDto.getTupleId());

        return ResponseEntity
                .noContent()
                .build();
    }
}
