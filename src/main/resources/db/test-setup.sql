CREATE SCHEMA IF NOT EXISTS hotela;

DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'hotela_admins') THEN
        CREATE ROLE hotela_admins NOLOGIN;
    END IF;
END
$$;

GRANT ALL ON SCHEMA hotela TO hotela_admins;

ALTER DEFAULT PRIVILEGES IN SCHEMA hotela FOR ROLE hotela_admins GRANT ALL ON TABLES TO hotela_admins;
ALTER DEFAULT PRIVILEGES IN SCHEMA hotela FOR ROLE hotela_admins GRANT ALL ON SEQUENCES TO hotela_admins;
ALTER DEFAULT PRIVILEGES IN SCHEMA hotela FOR ROLE hotela_admins GRANT ALL ON FUNCTIONS TO hotela_admins;
