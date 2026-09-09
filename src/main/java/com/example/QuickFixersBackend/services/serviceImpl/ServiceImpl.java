package com.example.QuickFixersBackend.services.serviceImpl;

import com.example.QuickFixersBackend.dto.service.ServiceRequistDTO;
import com.example.QuickFixersBackend.dto.service.ServiceResponseDTO;
import com.example.QuickFixersBackend.enums.ServiceStatut;
import com.example.QuickFixersBackend.mapper.ServiceMapper;
import com.example.QuickFixersBackend.entity.ServiceEntity;
import com.example.QuickFixersBackend.repository.ServiceRepository;
import com.example.QuickFixersBackend.services.serviceInterfce.ServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceImpl implements ServiceInterface {
    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;
    @Override
    public ServiceResponseDTO ajouterService(ServiceRequistDTO serviceRequistDTO) {
        ServiceEntity service = serviceMapper.toEntity(serviceRequistDTO);
        service.setStatut(ServiceStatut.ACTIVE);
        return serviceMapper.toDto(serviceRepository.save(service));
    }

    @Override
    public ServiceResponseDTO modifierService(Long id, ServiceRequistDTO serviceRequistDTO) {
        ServiceEntity serviceEntity = serviceRepository.findById(id).orElseThrow(()-> new RuntimeException("Service not found"));
        serviceEntity.setNom(serviceRequistDTO.getNom());
        serviceEntity.setDescription(serviceRequistDTO.getDescription());
        serviceEntity.setDureeEstimee(String.valueOf(serviceRequistDTO.getDureeEstimee()));
        serviceEntity.setPrix(serviceRequistDTO.getPrix());
        serviceEntity.setStatut(serviceRequistDTO.getStatut());
        serviceEntity.setType(serviceRequistDTO.getType());

        ServiceEntity modifieService = serviceRepository.save(serviceEntity);

        return serviceMapper.toDto(modifieService);
    }

    @Override
    public void supprimerService(Long id) {
        ServiceEntity serviceEntity = serviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Service not found"));
        serviceRepository.delete(serviceEntity);
    }

    @Override
    public Page<ServiceResponseDTO> listerServices(Pageable pageable) {
        return serviceRepository.findAll(pageable)
                .map(serviceMapper::toDto);
    }

    @Override
    public ServiceResponseDTO consulterUnService(Long id) {
        ServiceEntity serviceEntity = serviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Service not found"));
        return serviceMapper.toDto(serviceEntity);
    }

    @Override
    public ServiceResponseDTO updateStatus(Long id, ServiceStatut statut) {
        ServiceEntity serviceEntity = serviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Service not found"));
        serviceEntity.setStatut(statut);
        return serviceMapper.toDto(serviceRepository.save(serviceEntity));
    }
}

