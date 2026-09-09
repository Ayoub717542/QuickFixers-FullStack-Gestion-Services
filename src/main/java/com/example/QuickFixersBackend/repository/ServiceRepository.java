package com.example.QuickFixersBackend.repository;

import com.example.QuickFixersBackend.entity.ServiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<ServiceEntity,Integer> {
    Optional<ServiceEntity> findById(Long id);
    Page<ServiceEntity> findAll(Pageable pageable);
}
