package com.example.QuickFixersBackend.services.serviceInterfce;

import com.example.QuickFixersBackend.dto.service.ServiceRequistDTO;
import com.example.QuickFixersBackend.dto.service.ServiceResponseDTO;

import com.example.QuickFixersBackend.enums.ServiceStatut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServiceInterface  {
     ServiceResponseDTO ajouterService(ServiceRequistDTO serviceRequistDTO);
     ServiceResponseDTO modifierService(Long id, ServiceRequistDTO serviceRequistDTO);
     void supprimerService (Long id);
     Page<ServiceResponseDTO> listerServices(Pageable pageable);
     ServiceResponseDTO consulterUnService(Long id);
     ServiceResponseDTO updateStatus(Long id, ServiceStatut statut);
}