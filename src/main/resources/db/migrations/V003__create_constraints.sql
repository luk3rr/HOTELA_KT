--liquibase formatted sql

--changeset lucas.araujo:003-constraints
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
    ADD CONSTRAINT fk_review_booking FOREIGN KEY (booking_id) REFERENCES booking (id);
ALTER TABLE customer_auth
    ADD CONSTRAINT fk_customer_auth FOREIGN KEY (customer_id) REFERENCES customer (id);
ALTER TABLE partner_auth
    ADD CONSTRAINT fk_partner_auth FOREIGN KEY (partner_id) REFERENCES partner (id);

--rollback ALTER TABLE partner_auth DROP CONSTRAINT IF EXISTS fk_partner_auth;
--rollback ALTER TABLE customer_auth DROP CONSTRAINT IF EXISTS fk_customer_auth;
--rollback ALTER TABLE review DROP CONSTRAINT IF EXISTS fk_review_booking;
--rollback ALTER TABLE payment DROP CONSTRAINT IF EXISTS fk_payment_booking;
--rollback ALTER TABLE booking DROP CONSTRAINT IF EXISTS fk_booking_room;
--rollback ALTER TABLE booking DROP CONSTRAINT IF EXISTS fk_booking_customer;
--rollback ALTER TABLE room DROP CONSTRAINT IF EXISTS fk_room_hotel;
--rollback ALTER TABLE hotel DROP CONSTRAINT IF EXISTS fk_hotel_partner;
