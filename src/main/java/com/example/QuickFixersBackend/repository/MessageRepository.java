package com.example.QuickFixersBackend.repository;

import com.example.QuickFixersBackend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository  extends JpaRepository<Message,Long> {

}
