package com.example.QuickFixersBackend.repository;

import com.example.QuickFixersBackend.entity.User;

import com.example.QuickFixersBackend.enums.Statut;
import com.example.QuickFixersBackend.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;



public interface TicketRepository extends JpaRepository<Ticket,Long> {
    Page<Ticket> findAll(Pageable pageable);
    Page<Ticket> findByStatut(Statut statut, Pageable pageable);

    @Query("SELECT t From Ticket t where t.titre Like %:searchedTicket%")
    Page<Ticket> searchedTicket(String searchedTicket,Pageable pageable);
    Page<Ticket> findByCreatedBy(User user, Pageable pageable);

    @Override
    long count();
}
