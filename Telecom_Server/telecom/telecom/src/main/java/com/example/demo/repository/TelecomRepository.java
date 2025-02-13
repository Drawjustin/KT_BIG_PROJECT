package com.example.demo.repository;

import com.example.demo.entity.Telecom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelecomRepository extends JpaRepository<Telecom, Long> {
}
