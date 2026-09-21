SET @fk = (
    SELECT CONSTRAINT_NAME
    FROM information_schema.KEY_COLUMN_USAGE
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'ticket'
      AND COLUMN_NAME = 'created_by'
    LIMIT 1
);
SET @s = IF(@fk IS NULL, 'SELECT 1', CONCAT('ALTER TABLE ticket DROP FOREIGN KEY ', @fk));
PREPARE st FROM @s; EXECUTE st; DEALLOCATE PREPARE st;

ALTER TABLE ticket
    ADD CONSTRAINT fk_ticket_created_by_cascade
        FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE;


SET @fk = (
    SELECT CONSTRAINT_NAME
    FROM information_schema.KEY_COLUMN_USAGE
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'ticket'
      AND COLUMN_NAME = 'assigned_to'
    LIMIT 1
);
SET @s = IF(@fk IS NULL, 'SELECT 1', CONCAT('ALTER TABLE ticket DROP FOREIGN KEY ', @fk));
PREPARE st FROM @s; EXECUTE st; DEALLOCATE PREPARE st;

ALTER TABLE ticket
    ADD CONSTRAINT fk_ticket_assigned_to_cascade
        FOREIGN KEY (assigned_to) REFERENCES users(id) ON DELETE CASCADE;


SET @fk = (
    SELECT CONSTRAINT_NAME
    FROM information_schema.KEY_COLUMN_USAGE
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'paiement'
      AND COLUMN_NAME = 'user_id'
    LIMIT 1
);
SET @s = IF(@fk IS NULL, 'SELECT 1', CONCAT('ALTER TABLE paiement DROP FOREIGN KEY ', @fk));
PREPARE st FROM @s; EXECUTE st; DEALLOCATE PREPARE st;

ALTER TABLE paiement
    ADD CONSTRAINT fk_paiement_user_cascade
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;


SET @fk = (
    SELECT CONSTRAINT_NAME
    FROM information_schema.KEY_COLUMN_USAGE
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'paiement'
      AND COLUMN_NAME = 'ticket_id'
    LIMIT 1
);
SET @s = IF(@fk IS NULL, 'SELECT 1', CONCAT('ALTER TABLE paiement DROP FOREIGN KEY ', @fk));
PREPARE st FROM @s; EXECUTE st; DEALLOCATE PREPARE st;

ALTER TABLE paiement
    ADD CONSTRAINT fk_paiement_ticket_cascade
        FOREIGN KEY (ticket_id) REFERENCES ticket(id) ON DELETE CASCADE;


SET @fk = (
    SELECT CONSTRAINT_NAME
    FROM information_schema.KEY_COLUMN_USAGE
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'message'
      AND COLUMN_NAME = 'sender_id'
    LIMIT 1
);
SET @s = IF(@fk IS NULL, 'SELECT 1', CONCAT('ALTER TABLE message DROP FOREIGN KEY ', @fk));
PREPARE st FROM @s; EXECUTE st; DEALLOCATE PREPARE st;

ALTER TABLE message
    ADD CONSTRAINT fk_message_sender_cascade
        FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE;


SET @fk = (
    SELECT CONSTRAINT_NAME
    FROM information_schema.KEY_COLUMN_USAGE
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'message'
      AND COLUMN_NAME = 'receiver_id'
    LIMIT 1
);
SET @s = IF(@fk IS NULL, 'SELECT 1', CONCAT('ALTER TABLE message DROP FOREIGN KEY ', @fk));
PREPARE st FROM @s; EXECUTE st; DEALLOCATE PREPARE st;

ALTER TABLE message
    ADD CONSTRAINT fk_message_receiver_cascade
        FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE;


SET @fk = (
    SELECT CONSTRAINT_NAME
    FROM information_schema.KEY_COLUMN_USAGE
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'message'
      AND COLUMN_NAME = 'ticket_id'
    LIMIT 1
);
SET @s = IF(@fk IS NULL, 'SELECT 1', CONCAT('ALTER TABLE message DROP FOREIGN KEY ', @fk));
PREPARE st FROM @s; EXECUTE st; DEALLOCATE PREPARE st;

ALTER TABLE message
    ADD CONSTRAINT fk_message_ticket_cascade
        FOREIGN KEY (ticket_id) REFERENCES ticket(id) ON DELETE CASCADE;