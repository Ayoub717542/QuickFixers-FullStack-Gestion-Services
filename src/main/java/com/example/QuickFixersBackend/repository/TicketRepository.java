package com.example.QuickFixersBackend.repository;

import com.example.QuickFixersBackend.enums.Statut;
import com.example.QuickFixersBackend.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket,Integer> {
    Optional<Ticket> findById(Long id );
    Page<Ticket> findAll(Pageable pageable);
    Page<Ticket> findByStatut(Statut statut, Pageable pageable);

    @Query("SELECT t From Ticket t where t.titre Like %:searchedTicket%")
    Page<Ticket> searchedTicket(String searchedTicket,Pageable pageable);


}
