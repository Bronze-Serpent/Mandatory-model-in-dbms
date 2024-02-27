package com.barabanov.mandatory.model.dbms.controller;

import com.barabanov.mandatory.model.dbms.controller.dto.CreateTupleDto;
import com.barabanov.mandatory.model.dbms.controller.dto.DeleteTupleDto;
import com.barabanov.mandatory.model.dbms.service.dto.ReadTupleSecurityDto;
import com.barabanov.mandatory.model.dbms.controller.dto.UpdateTupleSecurityDto;
import com.barabanov.mandatory.model.dbms.service.iterface.DynamicTupleService;
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


    @PostMapping("/insert")
    @ResponseStatus(HttpStatus.CREATED)
    public ReadTupleSecurityDto insertTupleIntoDb(CreateTupleDto createTupleDto)
    {
        return dynamicTupleService.insertIntoDb(createTupleDto.getDbSecId(), createTupleDto.getSecureSql());
    }


    @PutMapping("/change/lvl")
    public ReadTupleSecurityDto changeTupleSecLvl(UpdateTupleSecurityDto updateTupleDto)
    {
        return dynamicTupleService.changeTupleSecLvl(
                updateTupleDto.getTableSecId(),
                updateTupleDto.getTupleId(),
                updateTupleDto.getNewSecurityLvl());
    }


    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTuple(DeleteTupleDto deleteTupleDto)
    {
        dynamicTupleService.deleteTupleInTable(deleteTupleDto.getTableSecId(), deleteTupleDto.getTupleId());

        return ResponseEntity
                .noContent()
                .build();
    }
}
