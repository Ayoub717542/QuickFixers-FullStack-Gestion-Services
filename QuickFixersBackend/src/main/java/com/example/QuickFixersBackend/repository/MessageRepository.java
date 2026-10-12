package com.example.QuickFixersBackend.repository;

import com.example.QuickFixersBackend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // Tous les messages d'un ticket, du plus ancien au plus recent.
    List<Message> findByTicketIdOrderByDateEnvoiAsc(Long ticketId);
}
