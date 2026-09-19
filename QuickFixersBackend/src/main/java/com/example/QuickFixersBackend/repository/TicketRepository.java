package com.example.QuickFixersBackend.repository;

import com.example.QuickFixersBackend.entity.User;

import com.example.QuickFixersBackend.enums.Statut;
import com.example.QuickFixersBackend.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;


public interface TicketRepository extends JpaRepository<Ticket,Long> {
    Page<Ticket> findAll(Pageable pageable);
    Page<Ticket> findByStatut(Statut statut, Pageable pageable);
    Page<Ticket> findByAssignedToAndStatut(User user, Statut statut, Pageable pageable);
    Page<Ticket> findByAssignedTo(User user, Pageable pageable);
    Page<Ticket> findByCreatedByAndStatut(User user, Statut statut, Pageable pageable);

    @Query("SELECT t From Ticket t where t.titre Like %:searchedTicket%")
    Page<Ticket> searchedTicket(String searchedTicket,Pageable pageable);

    Page<Ticket> findByCreatedBy(User user, Pageable pageable);

    @Query(" SELECT t FROM Ticket t  WHERE t.assignedTo = :user AND t.titre LIKE %:recherche%")
    Page<Ticket> searchedAssignedTickets(@Param("recherche") String recherche, @Param("user") User user, Pageable pageable);

    @Query(" SELECT t FROM Ticket t WHERE t.createdBy = :user AND t.titre LIKE %:recherche%")
    Page<Ticket> searchedUserTickets(@Param("recherche") String recherche, @Param("user") User user, Pageable pageable);

    @Override
    long count();

    long countByAssignedTo(User user);
    long countByCreatedBy(User user);
}
