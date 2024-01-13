--liquibase formatted sql

--changeset barabanov:dbms_user
CREATE TABLE dbms_user
(
    id              BIGSERIAL        PRIMARY KEY,
    name            VARCHAR(64)      UNIQUE,
    security_level  SECURITY_LEVEL   NOT NULL
);