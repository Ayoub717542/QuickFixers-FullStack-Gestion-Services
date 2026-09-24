package com.example.QuickFixersBackend.repository;

import com.example.QuickFixersBackend.entity.Paiement;
import com.example.QuickFixersBackend.entity.Person;
import com.example.QuickFixersBackend.entity.Ticket;
import com.example.QuickFixersBackend.enums.PaiementStatut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PaiementRepository  extends JpaRepository<Paiement,Long> {
    Page<Paiement> findByStatut(PaiementStatut statut, Pageable pageable);
    Page<Paiement> findByClient(Person client,Pageable pageable);
    @Override
    long count();

    long countByClient(Person client);
    long countByStatut(PaiementStatut statut);
    Page<Paiement> findByTicketAssignedTo(Person agent, Pageable pageable);

    @Query("select FUNCTION('DATE', p.dateCreation) AS jour, SUM(p.montant) AS total " +
            "from Paiement p where p.statut = :statut " +
            "group by FUNCTION('DATE', p.dateCreation) " +
            "order by FUNCTION('DATE', p.dateCreation)")
    List<Object[]> incomeByDay(@Param("statut") PaiementStatut statut);

    @Query("""
         SELECT FUNCTION('DATE', p.dateCreation), SUM(p.montant)
         FROM Paiement p
         JOIN p.ticket t
         WHERE p.statut = :statut AND t.assignedTo = :agent
         GROUP BY FUNCTION('DATE', p.dateCreation)
         ORDER BY FUNCTION('DATE', p.dateCreation)
         """)
    List<Object[]> incomeByDayForSupport(@Param("statut") PaiementStatut statut, @Param("agent") Person agent);

    boolean existsByTicketAndStatut(Ticket ticket, PaiementStatut statut);

//    @Query("SELECT count(p) FROM Paiement p WHERE p.client = :client")
//    long findByClient(@Param("client") Person client);

}