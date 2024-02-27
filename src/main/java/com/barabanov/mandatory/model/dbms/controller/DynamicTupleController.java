package com.barabanov.mandatory.model.dbms.controller;

import com.barabanov.mandatory.model.dbms.controller.dto.CreateTupleDto;
import com.barabanov.mandatory.model.dbms.controller.dto.DeleteTupleDto;
import com.barabanov.mandatory.model.dbms.controller.dto.UpdateTupleSecurityDto;
import com.barabanov.mandatory.model.dbms.service.iterface.SecureDynamicTupleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("api/v1/tuple")
@RestController
public class DynamicTupleController
{
    private final SecureDynamicTupleService dynamicTupleService;


    @PostMapping("/insert")
    public ResponseEntity<?> insertTupleIntoDb(CreateTupleDto createTupleDto)
    {
        dynamicTupleService.insertIntoDb(createTupleDto.getDbSecId(), createTupleDto.getSecureSql());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }


    @PutMapping("/change/lvl")
    public ResponseEntity<?> changeTupleSecLvl(UpdateTupleSecurityDto updateTupleDto)
    {
        dynamicTupleService.changeTupleSecLvl(
                updateTupleDto.getTableSecId(),
                updateTupleDto.getTupleId(),
                updateTupleDto.getNewSecurityLvl());
        return ResponseEntity
                .ok()
                .build();
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
