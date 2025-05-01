--liquibase formatted sql

--changeset lucas.araujo:002-create-tables
CREATE TABLE partner
(
    id              uuid PRIMARY KEY NOT NULL,
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

CREATE TABLE hotel
(
    id          uuid PRIMARY KEY NOT NULL,
    partner_id  uuid NOT NULL,
    name        varchar(255) NOT NULL,
    address     varchar(255) NOT NULL,
    city        varchar(100) NOT NULL,
    state       varchar(50) NOT NULL,
    zip_code    varchar(20) NOT NULL,
    phone       varchar(20) NOT NULL,
    rating      decimal(2, 1) NOT NULL DEFAULT 0,
    description text,
    website     varchar(255),
    latitude    decimal(10, 6) NOT NULL,
    longitude   decimal(10, 6) NOT NULL
);

CREATE TABLE room
(
    id          uuid PRIMARY KEY NOT NULL,
    hotel_id    uuid NOT NULL,
    number      varchar(10) NOT NULL,
    floor       int NOT NULL,
    type        varchar(50) NOT NULL,
    price       decimal(10, 2),
    capacity    int NOT NULL,
    status      room_status NOT NULL DEFAULT 'AVAILABLE',
    description text
);

CREATE TABLE customer
(
    id          uuid PRIMARY KEY NOT NULL,
    name        varchar(255) NOT NULL,
    email       varchar(255) UNIQUE NOT NULL,
    phone       varchar(20) NOT NULL,
    id_document varchar(50) NOT NULL,
    birth_date  date,
    address     varchar(255)
);

CREATE TABLE booking
(
    id          uuid PRIMARY KEY NOT NULL,
    customer_id uuid NOT NULL,
    hotel_id    uuid NOT NULL,
    room_id     uuid NOT NULL,
    booked_at   timestamp NOT NULL DEFAULT now(),
    checkin     timestamp NOT NULL,
    checkout    timestamp NOT NULL,
    guests      int NOT NULL,
    status      booking_status NOT NULL DEFAULT 'CONFIRMED',
    notes       text
);

CREATE TABLE payment
(
    id             uuid PRIMARY KEY NOT NULL,
    booking_id     uuid NOT NULL,
    transaction_id varchar(100) NOT NULL,
    amount         decimal(10, 2) NOT NULL,
    payment_method payment_method NOT NULL,
    status         payment_status NOT NULL DEFAULT 'PENDING',
    paid_at        timestamp NOT NULL DEFAULT now()
);

CREATE TABLE review
(
    id          uuid PRIMARY KEY NOT NULL,
    booking_id  uuid NOT NULL,
    rating      int NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment     text,
    reviewed_at timestamp NOT NULL DEFAULT now()
);

CREATE TABLE customer_auth
(
    id            uuid PRIMARY KEY NOT NULL,
    customer_id   uuid UNIQUE NOT NULL,
    email         varchar(255) UNIQUE NOT NULL,
    password_hash varchar(60)        NOT NULL,
    created_at    timestamp NOT NULL DEFAULT now(),
    last_login    timestamp NOT NULL DEFAULT now(),
    active        boolean NOT NULL DEFAULT true
);

CREATE TABLE partner_auth
(
    id            uuid PRIMARY KEY NOT NULL,
    partner_id    uuid UNIQUE NOT NULL,
    email         varchar(255) UNIQUE NOT NULL,
    password_hash varchar(255)        NOT NULL,
    created_at    timestamp NOT NULL DEFAULT now(),
    last_login    timestamp NOT NULL DEFAULT now(),
    active        boolean NOT NULL DEFAULT true
);

--rollback DROP TABLE IF EXISTS partner_auth;
--rollback DROP TABLE IF EXISTS customer_auth;
--rollback DROP TABLE IF EXISTS review;
--rollback DROP TABLE IF EXISTS payment;
--rollback DROP TABLE IF EXISTS booking;
--rollback DROP TABLE IF EXISTS customer;
--rollback DROP TABLE IF EXISTS room;
--rollback DROP TABLE IF EXISTS hotel;
--rollback DROP TABLE IF EXISTS partner;
