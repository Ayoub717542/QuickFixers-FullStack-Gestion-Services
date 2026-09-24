package com.example.QuickFixersBackend.repository;

import com.example.QuickFixersBackend.entity.Person;
import com.example.QuickFixersBackend.entity.Support;
import com.example.QuickFixersBackend.enums.ServiceType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<Person,Long> {

    Optional<Person> findByEmail(String email);

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
}