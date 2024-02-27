package com.barabanov.mandatory.model.dbms.dynamic.db.security.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class AbstractEntity<T extends Serializable>
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private T id;
}
