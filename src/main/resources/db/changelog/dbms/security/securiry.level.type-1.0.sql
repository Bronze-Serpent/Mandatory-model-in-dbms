--liquibase formatted sql

--changeset barabanov:SECURITY_LEVEL type
CREATE TYPE SECURITY_LEVEL AS ENUM
    (
        'NOT_SECRET',
        'SECRET',
        'TOP_SECRET',
        'OF_PARTICULAR_IMPORTANCE'
        );