--liquibase formatted sql

--preconditions onFail:HALT onError:HALT

--changeset lucas.araujo:000-setup

SET search_path TO hotela, public;
------------------------------------------------------------------------------------------------------------------------

GRANT ALL ON TABLE databasechangelog TO hotela_admins;
GRANT ALL ON TABLE databasechangeloglock TO hotela_admins;