--liquibase formatted sql

--changeset barabanov:database_security
CREATE TABLE database_security
(
    id              BIGSERIAL       PRIMARY KEY,
    name            VARCHAR(64)     UNIQUE,
    security_level  VARCHAR(64)  NOT NULL
);

--changeset barabanov:table_security
CREATE TABLE table_security
(
    id              BIGSERIAL       PRIMARY KEY,
    name            VARCHAR(64),
    database_id     BIGINT          REFERENCES database_security(id),
    security_level  VARCHAR(64)  NOT NULL,

    CONSTRAINT db_table_name_unique UNIQUE (database_id, name)
);

--changeset barabanov:column_security
CREATE TABLE column_security
(
    id              BIGSERIAL       PRIMARY KEY,
    name            VARCHAR(64),
    table_id        BIGINT          REFERENCES table_security(id),
    security_level  VARCHAR(64)  NOT NULL,

    CONSTRAINT table_column_name_unique UNIQUE (table_id, name)
);

--changeset barabanov:tuple_security
CREATE TABLE tuple_security
(
    id              BIGSERIAL       PRIMARY KEY,
    tuple_id        BIGINT,         -- id строки в таблице, для которой задаётся уровень безопасности
    table_id        BIGINT          REFERENCES table_security(id),
    security_level  VARCHAR(64)  NOT NULL,

    CONSTRAINT table_tuple_id_unique UNIQUE (table_id, tuple_id)
);

--changeset barabanov:attribute_security
CREATE TABLE value_security
(
    id              BIGSERIAL       PRIMARY KEY,
    tuple_id        BIGINT          REFERENCES tuple_security(id),
    column_id       BIGINT          REFERENCES column_security(id),
    security_level  VARCHAR(64)  NOT NULL,

    CONSTRAINT tuple_id_column_id_unique UNIQUE (tuple_id, column_id)
);

