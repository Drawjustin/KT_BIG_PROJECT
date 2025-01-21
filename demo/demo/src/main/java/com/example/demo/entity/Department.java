package com.example.demo.entity;

import com.example.demo.entity.baseEntity.baseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "department")
public class Department extends baseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_seq")
    private Long departmentSeq; // 부서 고유번호

    @Column(name = "department_name", nullable = false, length = 50)
    private String departmentName; // 부서 이름

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_seq", nullable = false)
    private Organization organization; // Organization 관계 설정
    @Builder
    public Department(String departmentName, Organization organization) {
        this.departmentName = departmentName;
        this.organization = organization;
    }
}