package com.example.QuickFixersBackend.webSocket;

import com.example.QuickFixersBackend.entity.Person;
import com.example.QuickFixersBackend.security.JwtService;
import com.example.QuickFixersBackend.services.TicketAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * Security for the chat WebSocket:
 *  - CONNECT : read the JWT sent by the frontend and log the user in.
 *  - SUBSCRIBE: only allow joining a ticket topic for its members
 *               (the rule itself lives in TicketAccessService).
 */
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private static final String TICKET_TOPIC_PREFIX = "/topic/ticket.";

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TicketAccessService ticketAccessService;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null || accessor.getCommand() == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            authenticate(accessor);
        }

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            authorizeSubscription(accessor);
        }

        return message;
    }

    /** Check the JWT and attach the logged-in user to the WebSocket session. */
    private void authenticate(StompHeaderAccessor accessor) {
        String authHeader = accessor.getFirstNativeHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing authentication token");
        }

        String token = authHeader.substring(7);
        String email;
        try {
            email = jwtService.extractUsername(token);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid authentication token");
        }

        Person person = (Person) userDetailsService.loadUserByUsername(email);

        if (!jwtService.isTokenValid(token, person)) {
            throw new IllegalArgumentException("Expired authentication token");
        }

        accessor.setUser(new UsernamePasswordAuthenticationToken(
                person, null, person.getAuthorities()));
    }

    /** Only ticket members may subscribe to a ticket conversation. */
    private void authorizeSubscription(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();

        if (destination == null || !destination.startsWith(TICKET_TOPIC_PREFIX)) {
            return; // not a ticket conversation
        }

        Principal principal = accessor.getUser();
        if (principal == null) {
            throw new AccessDeniedException("You must be logged in to join the chat");
        }

        Long ticketId = Long.valueOf(destination.substring(TICKET_TOPIC_PREFIX.length()));
        ticketAccessService.requireTicketMember(ticketId, principal.getName());
    }
}
