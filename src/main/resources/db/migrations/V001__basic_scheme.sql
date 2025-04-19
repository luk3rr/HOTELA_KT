--liquibase formatted sql

--changeset lucas:001

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;

CREATE TABLE example (
    id UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    name VARCHAR(255)     NOT NULL
);

--rollback DROP TABLE example;