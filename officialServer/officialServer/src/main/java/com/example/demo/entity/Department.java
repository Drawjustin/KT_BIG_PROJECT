package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "department")
public class Department extends baseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentSeq;

    private String departmentName;

    @Builder
    public Department(String departmentName) {
        this.departmentName = departmentName;
    }
}

