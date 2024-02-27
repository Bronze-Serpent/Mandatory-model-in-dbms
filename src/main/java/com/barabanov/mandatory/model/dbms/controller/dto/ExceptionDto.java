package com.barabanov.mandatory.model.dbms.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;


@Builder
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Data
public class ExceptionDto
{
    String message;
}
