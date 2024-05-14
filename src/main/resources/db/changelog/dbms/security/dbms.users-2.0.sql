--liquibase formatted sql


--changeset barabanov:abstract_user
CREATE TABLE abstract_user (
   id              BIGSERIAL        PRIMARY KEY,
   login           VARCHAR(64)      UNIQUE,
   password        VARCHAR(255)     NOT NULL
);

--changeset barabanov:super_user
CREATE TABLE super_user (
    id              BIGINT          PRIMARY KEY         REFERENCES abstract_user(id)
);


--changeset barabanov:dbms_admin
CREATE TABLE dbms_admin
(
    id              BIGINT          PRIMARY KEY          REFERENCES abstract_user(id)
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

--changeset barabanov:dbms_user
CREATE TABLE dbms_user
(
    id              BIGINT          PRIMARY KEY             REFERENCES abstract_user(id),
    security_level  VARCHAR(64)     NOT NULL
);