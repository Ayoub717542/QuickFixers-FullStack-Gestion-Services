package com.example.QuickFixersBackend.repository;

import com.example.QuickFixersBackend.entity.Person;

import com.example.QuickFixersBackend.enums.Priority;
import com.example.QuickFixersBackend.enums.Statut;
import com.example.QuickFixersBackend.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;


public interface TicketRepository extends JpaRepository<Ticket,Long> {
    @EntityGraph(attributePaths = {"assignedTo", "createdBy", "service"})
    Page<Ticket> findAll(Pageable pageable);
    @EntityGraph(attributePaths = {"assignedTo", "createdBy", "service"})
    Page<Ticket> findByStatut(Statut statut, Pageable pageable);
    @EntityGraph(attributePaths = {"assignedTo", "createdBy", "service"})
    Page<Ticket> findByAssignedToAndStatut(Person person, Statut statut, Pageable pageable);
    @EntityGraph(attributePaths = {"assignedTo", "createdBy", "service"})
    Page<Ticket> findByAssignedTo(Person person, Pageable pageable);
    @EntityGraph(attributePaths = {"assignedTo", "createdBy", "service"})
    Page<Ticket> findByCreatedByAndStatut(Person person, Statut statut, Pageable pageable);
    @EntityGraph(attributePaths = {"assignedTo", "createdBy", "service"})
    Page<Ticket> findByPriority(Priority priority, Pageable pageable);
    @EntityGraph(attributePaths = {"assignedTo", "createdBy", "service"})
    Page<Ticket> findByAssignedToAndPriority(Person person, Priority priority, Pageable pageable);
    @EntityGraph(attributePaths = {"assignedTo", "createdBy", "service"})
    Page<Ticket> findByCreatedByAndPriority(Person person, Priority priority, Pageable pageable);
    @EntityGraph(attributePaths = {"assignedTo", "createdBy", "service"})
    @Query("SELECT t From Ticket t where t.titre Like %:searchedTicket%")
    Page<Ticket> searchedTicket(String searchedTicket,Pageable pageable);

    @EntityGraph(attributePaths = {"assignedTo", "createdBy", "service"})
    Page<Ticket> findByCreatedBy(Person person, Pageable pageable);
    @EntityGraph(attributePaths = {"assignedTo", "createdBy", "service"})
    @Query(" SELECT t FROM Ticket t  WHERE t.assignedTo = :person AND t.titre LIKE %:recherche%")
    Page<Ticket> searchedAssignedTickets(@Param("recherche") String recherche, @Param("person") Person person, Pageable pageable);
    @EntityGraph(attributePaths = {"assignedTo", "createdBy", "service"})
    @Query(" SELECT t FROM Ticket t WHERE t.createdBy = :person AND t.titre LIKE %:recherche%")
    Page<Ticket> searchedUserTickets(@Param("recherche") String recherche, @Param("person") Person person, Pageable pageable);

    long countByAssignedTo(Person person);
    long countByCreatedBy(Person person);

//    Page<Ticket> findByDateCreationAndCreatedByAfterOrderById(Pageable pageable);

}