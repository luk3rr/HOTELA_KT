--liquibase formatted sql

--changeset lucas.araujo:001-create-types
CREATE TYPE partner_status AS ENUM ('ACTIVE', 'INACTIVE');
CREATE TYPE room_status AS ENUM ('AVAILABLE', 'BOOKED', 'MAINTENANCE');
CREATE TYPE payment_method AS ENUM ('CREDIT_CARD', 'DEBIT_CARD', 'PAYPAL', 'BANK_TRANSFER', 'PIX', 'CASH');
CREATE TYPE payment_status AS ENUM ('PENDING', 'COMPLETED', 'REFUNDED', 'FAILED');
CREATE TYPE booking_status AS ENUM ('CONFIRMED', 'CANCELLED', 'IN_PROGRESS', 'COMPLETED');

--rollback DROP TYPE IF EXISTS booking_status;
--rollback DROP TYPE IF EXISTS payment_status;
--rollback DROP TYPE IF EXISTS payment_method;
--rollback DROP TYPE IF EXISTS room_status;
--rollback DROP TYPE IF EXISTS partner_status;