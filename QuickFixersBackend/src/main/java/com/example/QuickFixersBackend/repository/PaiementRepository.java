package com.example.QuickFixersBackend.repository;

import com.example.QuickFixersBackend.entity.Paiement;
import com.example.QuickFixersBackend.entity.User;
import com.example.QuickFixersBackend.enums.PaiementStatut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PaiementRepository  extends JpaRepository<Paiement,Long> {
    Page<Paiement> findByStatut(PaiementStatut statut, Pageable pageable);
    Page<Paiement> findByUser(User user,Pageable pageable);
    @Override
    long count();

    long countByUser(User user);

    long countByStatut(PaiementStatut statut);

//    @Query("SELECT count(p) FROM Paiement p WHERE p.user = :user")
//    long findByUser(@Param("user") User user);

}
