--liquibase formatted sql

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;

--changeset lucas.araujo:003-create-partner
CREATE TABLE partner
(
    id              uuid PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    name            varchar(255) NOT NULL,
    cnpj            varchar(255) NOT NULL,
    email           varchar(255) UNIQUE NOT NULL,
    phone           varchar(20) NOT NULL,
    address         varchar(255) NOT NULL,
    contact_name    varchar(255),
    contact_email   varchar(255),
    contact_phone   varchar(20),
    contract_signed boolean NOT NULL DEFAULT false,
    status          partner_status NOT NULL DEFAULT 'ACTIVE',
    created_at      timestamp DEFAULT now(),
    notes           text
);
--rollback DROP TABLE IF EXISTS partner;

--changeset lucas.araujo:004-create-hotel
CREATE TABLE hotel
(
    id          uuid PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    partner_id  uuid NOT NULL,
    name        varchar(255) NOT NULL,
    address     varchar(255) NOT NULL,
    city        varchar(100) NOT NULL,
    state       varchar(50) NOT NULL,
    zip_code    varchar(20) NOT NULL,
    phone       varchar(20) NOT NULL,
    rating      decimal(1, 1) NOT NULL DEFAULT 0,
    description text,
    website     varchar(255),
    latitude    decimal(10, 6) NOT NULL,
    longitude   decimal(10, 6) NOT NULL
);
--rollback DROP TABLE IF EXISTS hotel;

--changeset lucas.araujo:005-create-room
CREATE TABLE room
(
    id          uuid PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    hotel_id    uuid NOT NULL,
    number      varchar(10) NOT NULL,
    floor       int NOT NULL,
    type        varchar(50) NOT NULL,
    price       decimal(10, 2),
    capacity    int NOT NULL,
    status      room_status NOT NULL DEFAULT 'AVAILABLE',
    description text
);
--rollback DROP TABLE IF EXISTS room;

--changeset lucas.araujo:006-create-customer
CREATE TABLE customer
(
    id          uuid PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    name        varchar(255) NOT NULL,
    email       varchar(255) UNIQUE NOT NULL,
    phone       varchar(20) NOT NULL,
    id_document varchar(50) NOT NULL,
    birth_date  date,
    address     varchar(255)
);
--rollback DROP TABLE IF EXISTS customer;

--changeset lucas.araujo:007-create-booking
CREATE TABLE booking
(
    id          uuid PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    customer_id uuid NOT NULL,
    room_id     uuid NOT NULL,
    booked_at   timestamp NOT NULL DEFAULT now(),
    checkin     timestamp NOT NULL,
    checkout    timestamp NOT NULL,
    guests      int NOT NULL,
    status      booking_status NOT NULL DEFAULT 'CONFIRMED',
    notes       text
);
--rollback DROP TABLE IF EXISTS booking;

--changeset lucas.araujo:008-create-payment
CREATE TABLE payment
(
    id             uuid PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    booking_id     uuid NOT NULL,
    transaction_id varchar(100) NOT NULL,
    amount         decimal(10, 2) NOT NULL,
    payment_method payment_method NOT NULL,
    status         payment_status NOT NULL DEFAULT 'PENDING',
    paid_at        timestamp NOT NULL DEFAULT now()
);
--rollback DROP TABLE IF EXISTS payment;

--changeset lucas.araujo:009-create-review
CREATE TABLE review
(
    id          uuid PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    customer_id uuid NOT NULL,
    hotel_id    uuid NOT NULL,
    room_id     uuid NOT NULL,
    rating      int NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment     text,
    reviewed_at timestamp NOT NULL DEFAULT now()
);
--rollback DROP TABLE IF EXISTS review;

--changeset lucas.araujo:010-create-customer-auth
CREATE TABLE customer_auth
(
    id            uuid PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    customer_id   uuid UNIQUE NOT NULL,
    email         varchar(255) UNIQUE NOT NULL,
    password_hash varchar(60)        NOT NULL,
    created_at    timestamp NOT NULL DEFAULT now(),
    last_login    timestamp NOT NULL DEFAULT now(),
    active        boolean NOT NULL DEFAULT true
);
--rollback DROP TABLE IF EXISTS customer_auth;

--changeset lucas.araujo:011-create-partner-auth
CREATE TABLE partner_auth
(
    id            uuid PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    partner_id    uuid UNIQUE NOT NULL,
    email         varchar(255) UNIQUE NOT NULL,
    password_hash varchar(255)        NOT NULL,
    created_at    timestamp NOT NULL DEFAULT now(),
    last_login    timestamp NOT NULL DEFAULT now(),
    active        boolean NOT NULL DEFAULT true
);
--rollback DROP TABLE IF EXISTS partner_auth;

--changeset lucas.araujo:012-foreign-keys
ALTER TABLE hotel
    ADD CONSTRAINT fk_hotel_partner FOREIGN KEY (partner_id) REFERENCES partner (id);
ALTER TABLE room
    ADD CONSTRAINT fk_room_hotel FOREIGN KEY (hotel_id) REFERENCES hotel (id);
ALTER TABLE booking
    ADD CONSTRAINT fk_booking_customer FOREIGN KEY (customer_id) REFERENCES customer (id);
ALTER TABLE booking
    ADD CONSTRAINT fk_booking_room FOREIGN KEY (room_id) REFERENCES room (id);
ALTER TABLE payment
    ADD CONSTRAINT fk_payment_booking FOREIGN KEY (booking_id) REFERENCES booking (id);
ALTER TABLE review
    ADD CONSTRAINT fk_review_customer FOREIGN KEY (customer_id) REFERENCES customer (id);
ALTER TABLE review
    ADD CONSTRAINT fk_review_hotel FOREIGN KEY (hotel_id) REFERENCES hotel (id);
ALTER TABLE review
    ADD CONSTRAINT fk_review_room FOREIGN KEY (room_id) REFERENCES room (id);
ALTER TABLE customer_auth
    ADD CONSTRAINT fk_customer_auth FOREIGN KEY (customer_id) REFERENCES customer (id);
ALTER TABLE partner_auth
    ADD CONSTRAINT fk_partner_auth FOREIGN KEY (partner_id) REFERENCES partner (id);
--rollback ALTER TABLE partner_auth DROP CONSTRAINT IF EXISTS fk_partner_auth;
--rollback ALTER TABLE customer_auth DROP CONSTRAINT IF EXISTS fk_customer_auth;
--rollback ALTER TABLE review DROP CONSTRAINT IF EXISTS fk_review_room;
--rollback ALTER TABLE review DROP CONSTRAINT IF EXISTS fk_review_hotel;
--rollback ALTER TABLE review DROP CONSTRAINT IF EXISTS fk_review_customer;
--rollback ALTER TABLE payment DROP CONSTRAINT IF EXISTS fk_payment_booking;
--rollback ALTER TABLE booking DROP CONSTRAINT IF EXISTS fk_booking_room;
--rollback ALTER TABLE booking DROP CONSTRAINT IF EXISTS fk_booking_customer;
--rollback ALTER TABLE room DROP CONSTRAINT IF EXISTS fk_room_hotel;
--rollback ALTER TABLE hotel DROP CONSTRAINT IF EXISTS fk_hotel_partner;
