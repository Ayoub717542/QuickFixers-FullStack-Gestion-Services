package com.example.QuickFixersBackend.dto.message;

import lombok.Getter;
import lombok.Setter;

/**
 * What the frontend sends when a user types a message.
 * It intentionally contains no sender id/email: the sender is always taken
 * from the authenticated user (the JWT) on the backend.
 */
@Getter
@Setter
public class MessageRequestDTO {
    private String contenu;
    private Long ticketId;
}
