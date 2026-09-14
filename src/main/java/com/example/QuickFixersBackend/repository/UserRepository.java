package com.example.QuickFixersBackend.repository;

import com.example.QuickFixersBackend.entity.User;
import com.example.QuickFixersBackend.enums.ServiceType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
Optional<User>  findByEmail(String email);
Optional<User> findById(Long id);
Page<User> findAll(Pageable pageable);
boolean existsByEmail(@NotBlank @Email String email);

    @Query("""
    SELECT u FROM User u
    WHERE u.role = 'SUPPORT' AND u.serviceType = :serviceType
    ORDER BY (
        SELECT COUNT(t) FROM Ticket t
        WHERE t.assignedTo = u AND t.statut != 'FERME'
    ) ASC
    """)
    List<User> findSupportOrderByOpenTicketsAsc(@Param("serviceType") ServiceType serviceType);
}
