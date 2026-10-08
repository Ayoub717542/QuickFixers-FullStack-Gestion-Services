package com.example.QuickFixersBackend.services.serviceImpl;

import com.example.QuickFixersBackend.dto.service.ServiceRequistDTO;
import com.example.QuickFixersBackend.dto.service.ServiceResponseDTO;
import com.example.QuickFixersBackend.entity.Person;
import com.example.QuickFixersBackend.exception.NotFoundException;
import com.example.QuickFixersBackend.enums.ServiceStatut;
import com.example.QuickFixersBackend.mapper.ServiceMapper;
import com.example.QuickFixersBackend.entity.ServiceEntity;
import com.example.QuickFixersBackend.repository.ServiceRepository;
import com.example.QuickFixersBackend.repository.UserRepository;
import com.example.QuickFixersBackend.services.serviceInterfce.ServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;


@Service
@RequiredArgsConstructor
public class ServiceImpl implements ServiceInterface {
    private static final String SERVICE_NOT_FOUND = "Service not found";
    private final ServiceRepository serviceRepository;
    private final ServiceMapper serviceMapper;
    private final UserRepository userRepository;

    @Override
    @CacheEvict(value = {"services", "serviceDetails", "serviceCount"}, allEntries = true)
    public ServiceResponseDTO ajouterService(ServiceRequistDTO serviceRequistDTO,String email) {

        Person createdBy = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        ServiceEntity service = serviceMapper.toEntity(serviceRequistDTO);
        service.setStatut(ServiceStatut.ACTIVE);
        service.setCreatedBy(createdBy);

        return serviceMapper.toDto(serviceRepository.save(service));
    }

    @Override
    @CacheEvict(value = {"services", "serviceDetails", "serviceCount"}, allEntries = true)
    public ServiceResponseDTO modifierService(Long id, ServiceRequistDTO serviceRequistDTO) {
        ServiceEntity serviceEntity = serviceRepository.findById(id).orElseThrow(()-> new NotFoundException(SERVICE_NOT_FOUND));
        serviceEntity.setNom(serviceRequistDTO.getNom());
        serviceEntity.setStatut(serviceRequistDTO.getStatut());
        serviceEntity.setType(serviceRequistDTO.getType());

        ServiceEntity modifieService = serviceRepository.save(serviceEntity);

        return serviceMapper.toDto(modifieService);
    }

    @Override
    @CacheEvict(value = {"services", "serviceDetails", "serviceCount"}, allEntries = true)
    public void supprimerService(Long id) {
        ServiceEntity serviceEntity = serviceRepository.findById(id).orElseThrow(() -> new NotFoundException(SERVICE_NOT_FOUND));
        serviceRepository.delete(serviceEntity);
    }

    @Override
    @Cacheable(value = "services", key = "#p0.toString()")
    public Page<ServiceResponseDTO> listerServices(Pageable pageable) {
        return serviceRepository.findAll(pageable)
                .map(serviceMapper::toDto);
    }

    @Override
    @Cacheable(value = "serviceDetails", key = "#p0")
    public ServiceResponseDTO consulterUnService(Long id) {
        ServiceEntity serviceEntity = serviceRepository.findById(id).orElseThrow(() -> new NotFoundException(SERVICE_NOT_FOUND));
        return serviceMapper.toDto(serviceEntity);
    }

    @Override
    @CacheEvict(value = {"services", "serviceDetails", "serviceCount"}, allEntries = true)
    public ServiceResponseDTO updateStatus(Long id, ServiceStatut statut) {
        ServiceEntity serviceEntity = serviceRepository.findById(id).orElseThrow(() -> new NotFoundException(SERVICE_NOT_FOUND));
        serviceEntity.setStatut(statut);
        return serviceMapper.toDto(serviceRepository.save(serviceEntity));
    }
    @Override
    @Cacheable(value = "serviceCount")
    public long countServices() {
        return serviceRepository.countByStatut(ServiceStatut.ACTIVE);
    }
}

