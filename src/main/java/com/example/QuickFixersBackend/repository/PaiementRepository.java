package com.example.QuickFixersBackend.repository;

import com.example.QuickFixersBackend.entity.Paiement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaiementRepository  extends JpaRepository<Paiement,Integer> {
    Optional<Paiement> findById(Long id);

    @Query("SELECT p from Paiement p where p.ticket.createdBy.id = :userId order by p.dateCreation DESC ")
    Page<Paiement> findByUserId(@Param("userId") Long userId, Pageable pageable);

}
