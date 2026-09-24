package com.example.QuickFixersBackend.repository;

import com.example.QuickFixersBackend.entity.Client;
import com.example.QuickFixersBackend.entity.Person;
import com.example.QuickFixersBackend.entity.Support;
import com.example.QuickFixersBackend.enums.ServiceType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<Person,Long> {

    Optional<Person> findByEmail(String email);

    @Query("SELECT c from Client c")
    Page<Client> findAllClients(Pageable  pageable);

    @Query("SELECT s FROM Support s")
    Page<Support> findAllSupports(Pageable pageable);

    boolean existsByEmail(@NotBlank @Email String email);
    @Query("""
    SELECT s FROM Support s
    WHERE s.serviceType = :serviceType
    ORDER BY (
        SELECT COUNT(t) FROM Ticket t
        WHERE t.assignedTo = s AND t.statut != 'FERME'
    ) ASC
    """)
    List<Support> findSupportOrderByOpenTicketsAsc(@Param("serviceType") ServiceType serviceType);

    @Override
    long count();

    @Modifying
    @Query(value = "UPDATE users SET role = :role, service_type = :serviceType WHERE id = :id",
            nativeQuery = true)
    void changerRole(@Param("id") Long id,
                           @Param("role") String role,
                           @Param("serviceType") String serviceType);


    @Query("SELECT s FROM Support s WHERE s.email LIKE %:searchedEmail%")
    Page<Support> searchedSupport(String searchedEmail, Pageable pageable);

    @Query("SELECT s FROM Support s WHERE s.serviceType = :serviceType")
    Page<Support> findSupportsByServiceType(
            ServiceType serviceType,
            Pageable pageable
    );

    @Query("SELECT c FROM Client c WHERE c.nom LIKE %:nom%")
    Page<Client> rechercherClients(@Param("nom") String nom, Pageable pageable);


}