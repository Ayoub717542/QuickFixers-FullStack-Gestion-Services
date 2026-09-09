package com.example.QuickFixersBackend.services.serviceInterfce;

import com.example.QuickFixersBackend.dto.paiement.PaiementRequestDTO;
import com.example.QuickFixersBackend.dto.paiement.PaiementResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaiementInterface {
    PaiementResponseDTO creerPaiement(PaiementRequestDTO paiementRequestDTO);
    Page<PaiementResponseDTO> paimentHistorique(Long userId , Pageable pageable);


}
