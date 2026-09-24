package com.example.QuickFixersBackend.services.serviceInterfce;

import com.example.QuickFixersBackend.dto.paiement.IncomeByDay;
import com.example.QuickFixersBackend.dto.paiement.PaiementRequestDTO;
import com.example.QuickFixersBackend.dto.paiement.PaiementResponseDTO;
import com.example.QuickFixersBackend.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaiementInterface {
    PaiementResponseDTO creerPaiement(PaiementRequestDTO paiementRequestDTO, String email);
    Page<PaiementResponseDTO> paimentHistorique(Person person, Pageable pageable);
    long countPayments(Person person);
    List<IncomeByDay> incomeByday(Person person);
    byte[] genererRecu(Long paiementId);


}
