--liquibase formatted sql

--preconditions onFail:HALT onError:HALT

--changeset lucas.araujo:000-setup

SET search_path TO hotela, public;
------------------------------------------------------------------------------------------------------------------------

CREATE SCHEMA IF NOT EXISTS hotela;