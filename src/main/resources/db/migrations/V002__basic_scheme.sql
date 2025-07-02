--liquibase formatted sql

--preconditions onFail:HALT onError:HALT

--changeset lucas.araujo:002-create-tables

SET search_path TO hotela;
------------------------------------------------------------------------------------------------------------------------

CREATE TABLE auth_credential
(
    id            UUID PRIMARY KEY,
    login_email   VARCHAR(255) UNIQUE NOT NULL,
    password      VARCHAR(255)        NOT NULL,
    role          USER_ROLE           NOT NULL,
    is_active     BOOLEAN             NOT NULL DEFAULT true,
    last_login_at TIMESTAMPTZ,
    created_at    TIMESTAMPTZ         NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ
);

CREATE TABLE address
(
    id             UUID PRIMARY KEY,
    street_address VARCHAR(255) NOT NULL,
    number         VARCHAR(16),
    complement     VARCHAR(128),
    neighborhood   VARCHAR(128),
    city           VARCHAR(128) NOT NULL,
    state_province VARCHAR(128) NOT NULL,
    postal_code    VARCHAR(32)  NOT NULL,
    country_code   VARCHAR(2)   NOT NULL,
    latitude       DECIMAL(10, 8),
    longitude      DECIMAL(11, 8)
);

CREATE TABLE partner
(
    id                 UUID PRIMARY KEY,
    auth_credential_id UUID UNIQUE    NOT NULL REFERENCES auth_credential (id),
    address_id         UUID REFERENCES address (id),
    company_name       VARCHAR(255),
    legal_name         VARCHAR(255)   NOT NULL,
    email              VARCHAR(255) UNIQUE,
    phone              VARCHAR(32)    NOT NULL,
    document_id_type   VARCHAR(32),
    document_id_value  VARCHAR(64),
    contract_signed_at TIMESTAMPTZ,
    status             PARTNER_STATUS NOT NULL,
    notes              TEXT,

    UNIQUE (document_id_type, document_id_value)
);

CREATE TABLE hotel
(
    id          UUID PRIMARY KEY,
    partner_id  UUID         NOT NULL REFERENCES partner (id),
    address_id  UUID         NOT NULL REFERENCES address (id),
    name        VARCHAR(255) NOT NULL,
    phone       VARCHAR(32)  NOT NULL,
    email       VARCHAR(255),
    website     VARCHAR(255),
    description TEXT,
    star_rating DECIMAL(2, 1)
);

CREATE TABLE room_type
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(255) UNIQUE NOT NULL,
    description TEXT
);

CREATE TABLE room
(
    id              UUID PRIMARY KEY,
    hotel_id        UUID           NOT NULL REFERENCES hotel (id),
    room_type_id    UUID           NOT NULL REFERENCES room_type (id),
    room_code       VARCHAR(32)    NOT NULL,
    floor           INT,
    price_per_night DECIMAL(10, 2) NOT NULL,
    capacity        INT            NOT NULL,
    status          ROOM_STATUS    NOT NULL DEFAULT 'AVAILABLE',
    description     TEXT,

    UNIQUE (hotel_id, room_code)
);

CREATE TABLE customer
(
    id                 UUID PRIMARY KEY,
    auth_credential_id UUID         NOT NULL UNIQUE REFERENCES auth_credential (id),
    name               VARCHAR(255) NOT NULL,
    phone              VARCHAR(18)  NOT NULL,
    email              VARCHAR(255) UNIQUE,
    birth_date         DATE         NOT NULL,
    document_id_type   VARCHAR(50),
    document_id_value  VARCHAR(50),
    main_address_id    UUID REFERENCES address (id),

    UNIQUE (document_id_type, document_id_value)
);

CREATE TABLE booking
(
    id               UUID PRIMARY KEY,
    customer_id      UUID           NOT NULL REFERENCES customer (id),
    hotel_id         UUID           NOT NULL REFERENCES hotel (id),
    room_id          UUID           NOT NULL REFERENCES room (id),
    checkin_date     DATE           NOT NULL,
    checkout_date    DATE           NOT NULL,
    number_of_guests INT            NOT NULL DEFAULT 1,
    status           BOOKING_STATUS NOT NULL DEFAULT 'PENDING_CONFIRMATION',
    special_requests TEXT,
    booked_at        TIMESTAMPTZ    NOT NULL DEFAULT now()
);

CREATE TABLE payment
(
    id                      UUID PRIMARY KEY,
    booking_id              UUID           NOT NULL REFERENCES booking (id),
    external_transaction_id VARCHAR(255)   NOT NULL UNIQUE,
    amount_paid             DECIMAL(10, 2) NOT NULL,
    payment_method          PAYMENT_METHOD NOT NULL,
    status                  PAYMENT_STATUS NOT NULL DEFAULT 'PENDING',
    created_at              TIMESTAMPTZ    NOT NULL DEFAULT now(),
    processed_at            TIMESTAMPTZ,
    metadata                JSONB
);

CREATE TABLE review
(
    id           UUID PRIMARY KEY,
    booking_id   UUID        NOT NULL UNIQUE REFERENCES booking (id),
    customer_id  UUID        NOT NULL REFERENCES customer (id),
    hotel_id     UUID        NOT NULL REFERENCES hotel (id),
    rating       INT         NOT NULL,
    title        VARCHAR(255),
    comment      TEXT,
    is_anonymous BOOLEAN     NOT NULL DEFAULT false,
    reviewed_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at   TIMESTAMPTZ
);

--rollback DROP TABLE IF EXISTS review;
--rollback DROP TABLE IF EXISTS payment;
--rollback DROP TABLE IF EXISTS booking;
--rollback DROP TABLE IF EXISTS customer;
--rollback DROP TABLE IF EXISTS room;
--rollback DROP TABLE IF EXISTS room_type;
--rollback DROP TABLE IF EXISTS hotel;
--rollback DROP TABLE IF EXISTS partner;
--rollback DROP TABLE IF EXISTS address;
--rollback DROP TABLE IF EXISTS auth_credential;
