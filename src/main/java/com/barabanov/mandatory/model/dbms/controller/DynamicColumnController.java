package com.barabanov.mandatory.model.dbms.controller;

import com.barabanov.mandatory.model.dbms.entity.SecurityLevel;
import com.barabanov.mandatory.model.dbms.service.iterface.SecureDynamicColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("api/v1/column")
@RestController
public class DynamicColumnController
{
    private final SecureDynamicColumnService dynamicColumnService;


    @PutMapping("{columnId}")
    public ResponseEntity<?> changeColumnSecLvl(@PathVariable Long columnId,
                                             @RequestParam SecurityLevel newColumnSecLvl)
    {
        dynamicColumnService.changeColumnSecLvl(columnId, newColumnSecLvl);

        return ResponseEntity
                .ok()
                .build();
    }
}
