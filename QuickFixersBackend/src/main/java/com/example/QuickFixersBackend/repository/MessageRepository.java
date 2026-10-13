package com.example.QuickFixersBackend.repository;

import com.example.QuickFixersBackend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // Tous les messages d'un ticket, du plus ancien au plus recent.
    List<Message> findByTicketIdOrderByDateEnvoiAsc(Long ticketId);

    // Messages recus par cet utilisateur et pas encore lus, du plus recent au plus ancien.
    List<Message> findTop50ByReceiverEmailAndLuFalseOrderByDateEnvoiDesc(String email);

    // Marque comme lus tous les messages recus par cet utilisateur sur un ticket.
    @Modifying
    @Transactional
    @Query("update Message m set m.lu = true " +
            "where m.ticket.id = :ticketId and m.receiver.email = :email and m.lu = false")
    int markTicketRead(@Param("ticketId") Long ticketId, @Param("email") String email);
}
