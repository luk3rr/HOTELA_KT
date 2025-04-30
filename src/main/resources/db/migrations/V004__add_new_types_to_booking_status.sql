--liquibase formatted sql

--changeset lucas.araujo:013-alter-booking-status-enum
ALTER TYPE booking_status ADD VALUE IF NOT EXISTS 'IN_PROGRESS';
ALTER TYPE booking_status ADD VALUE IF NOT EXISTS 'COMPLETED';

--rollback ISN'T SUPPORTED: Cannot remove values from PostgreSQL enum types
