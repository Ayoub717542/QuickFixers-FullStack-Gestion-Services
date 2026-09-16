package com.example.QuickFixersBackend.repository;

import com.example.QuickFixersBackend.entity.ServiceEntity;
import com.example.QuickFixersBackend.enums.ServiceStatut;
import com.example.QuickFixersBackend.enums.ServiceType;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<ServiceEntity,Long> {
    Page<ServiceEntity> findAll(Pageable pageable);
    long countByStatut(ServiceStatut statut);
}
