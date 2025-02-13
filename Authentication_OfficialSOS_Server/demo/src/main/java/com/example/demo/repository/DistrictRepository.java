package com.example.demo.repository;

import com.example.demo.entity.Department;
import com.example.demo.entity.District;
import com.example.demo.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Long> {
    List<District> findAll();

}
