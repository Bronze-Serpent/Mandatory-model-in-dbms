--liquibase formatted sql

--changeset barabanov:dbms_admin
CREATE TABLE dbms_admin
(
    id              BIGSERIAL        PRIMARY KEY,
    name            VARCHAR(64)      UNIQUE
);

--changeset barabanov:database_ownership
CREATE TABLE database_ownership
(
    id              BIGSERIAL        PRIMARY KEY,
    admin_id        BIGINT           REFERENCES dbms_admin(id),
    database_id     BIGINT           REFERENCES database_security(id)
);

--changeset barabanov:database_administrators
CREATE TABLE database_administrator
(
    id              BIGSERIAL        PRIMARY KEY,
    admin_id        BIGINT           REFERENCES dbms_admin(id),
    database_id     BIGINT           REFERENCES database_security(id)
);