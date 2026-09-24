package com.example.QuickFixersBackend.repository;

import com.example.QuickFixersBackend.entity.Person;

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
    Page<Ticket> findByAssignedToAndStatut(Person person, Statut statut, Pageable pageable);
    Page<Ticket> findByAssignedTo(Person person, Pageable pageable);
    Page<Ticket> findByCreatedByAndStatut(Person person, Statut statut, Pageable pageable);

    @Query("SELECT t From Ticket t where t.titre Like %:searchedTicket%")
    Page<Ticket> searchedTicket(String searchedTicket,Pageable pageable);

    Page<Ticket> findByCreatedBy(Person person, Pageable pageable);

    @Query(" SELECT t FROM Ticket t  WHERE t.assignedTo = :person AND t.titre LIKE %:recherche%")
    Page<Ticket> searchedAssignedTickets(@Param("recherche") String recherche, @Param("person") Person person, Pageable pageable);

    @Query(" SELECT t FROM Ticket t WHERE t.createdBy = :person AND t.titre LIKE %:recherche%")
    Page<Ticket> searchedUserTickets(@Param("recherche") String recherche, @Param("person") Person person, Pageable pageable);

    @Override
    long count();

    long countByAssignedTo(Person person);
    long countByCreatedBy(Person person);
}