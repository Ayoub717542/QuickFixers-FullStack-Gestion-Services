package com.example.QuickFixersBackend.repository;

import com.example.QuickFixersBackend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @EntityGraph(attributePaths = {"sender", "receiver", "ticket"})
    List<Message> findByTicketIdOrderByDateEnvoiAsc(Long ticketId);
    @EntityGraph(attributePaths = {"sender", "receiver", "ticket"})
    List<Message> findTop50ByReceiverEmailAndLuFalseOrderByDateEnvoiDesc(String email);

    @Modifying
    @Transactional
    @Query("update Message m set m.lu = true " +
            "where m.ticket.id = :ticketId and m.receiver.email = :email and m.lu = false")
    int markTicketRead(@Param("ticketId") Long ticketId, @Param("email") String email);
}
