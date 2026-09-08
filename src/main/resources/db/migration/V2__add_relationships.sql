ALTER TABLE ticket
    ADD CONSTRAINT fk_ticket_user
        FOREIGN KEY (assigned_to) REFERENCES users(id);

ALTER TABLE ticket
    ADD COLUMN service_id BIGINT;

ALTER TABLE ticket
    ADD CONSTRAINT fk_ticket_service
        FOREIGN KEY (service_id) REFERENCES serviceEntity(id);

ALTER TABLE payment
    ADD COLUMN ticket_id BIGINT;

ALTER TABLE payment
    ADD CONSTRAINT fk_payment_ticket
        FOREIGN KEY (ticket_id) REFERENCES ticket(id);

ALTER TABLE payment
    ADD COLUMN user_id BIGINT;

ALTER TABLE payment
    ADD CONSTRAINT fk_payment_user
        FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE message
    ADD COLUMN ticket_id BIGINT;

ALTER TABLE message
    ADD CONSTRAINT fk_message_ticket
        FOREIGN KEY (ticket_id) REFERENCES ticket(id);

ALTER TABLE message
    ADD COLUMN user_id BIGINT;

ALTER TABLE message
    ADD CONSTRAINT fk_message_user
        FOREIGN KEY (user_id) REFERENCES users(id);