ALTER TABLE ticket
    ADD CONSTRAINT fk_ticket_assigned_to
        FOREIGN KEY (assigned_to) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE ticket
    ADD CONSTRAINT fk_ticket_created_by
        FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE ticket
    ADD CONSTRAINT fk_ticket_service
        FOREIGN KEY (service_id) REFERENCES services(id);

ALTER TABLE services
    ADD CONSTRAINT fk_service_created_by
        FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE paiement
    ADD CONSTRAINT fk_paiement_ticket
        FOREIGN KEY (ticket_id) REFERENCES ticket(id) ON DELETE CASCADE;

ALTER TABLE paiement
    ADD CONSTRAINT fk_paiement_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE message
    ADD CONSTRAINT fk_message_sender
        FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE message
    ADD CONSTRAINT fk_message_receiver
        FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE message
    ADD CONSTRAINT fk_message_ticket
        FOREIGN KEY (ticket_id) REFERENCES ticket(id) ON DELETE CASCADE;