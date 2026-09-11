package com.example.QuickFixersBackend.repository;

import com.example.QuickFixersBackend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
Optional<User>  findByEmail(String email);
Optional<User> findById(Long id);
Page<User> findAll(Pageable pageable);
}
